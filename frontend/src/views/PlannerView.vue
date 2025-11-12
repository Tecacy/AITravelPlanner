<template>
  <section class="planner-view">
    <div class="planner-view__form panel">
      <h2>个性化旅行需求</h2>
      <el-form :model="form" label-position="top" @submit.prevent>
        <el-form-item label="目的地">
          <el-input v-model="form.destination" placeholder="例如：日本东京" />
        </el-form-item>
        <el-form-item label="出发日期">
          <el-date-picker
            v-model="form.dates"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item label="预算 (元)">
          <el-input-number v-model="form.budget" :min="0" :step="500" />
        </el-form-item>
        <el-form-item label="同行人数">
          <el-input-number v-model="form.travelers" :min="1" />
        </el-form-item>
        <el-form-item label="旅行偏好">
          <el-select
            v-model="form.preferences"
            multiple
            filterable
            placeholder="选择偏好"
          >
            <el-option v-for="item in preferenceOptions" :key="item" :value="item" :label="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleGenerate">
            生成AI行程
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <VoiceInputCard @transcribed="handleVoiceInput" />
    </div>
    <div class="planner-view__result panel">
      <ItinerarySummary :plan="currentPlan" :loading="loading" />
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

import ItinerarySummary from '@/components/planner/ItinerarySummary.vue';
import VoiceInputCard from '@/components/voice/VoiceInputCard.vue';
import { useItineraryStore } from '@/stores';

const itineraryStore = useItineraryStore();
const loading = ref(false);
const currentPlan = computed(() => itineraryStore.currentPlan);

const form = reactive({
  destination: '',
  dates: [] as [Date, Date] | [],
  budget: 10000,
  travelers: 2,
  preferences: [] as string[],
});

const preferenceOptions = ['美食', '亲子', '自然风光', '历史文化', '购物', '冒险刺激', '动漫'];

const handleGenerate = async () => {
  if (!form.destination || form.dates.length !== 2) {
    ElMessage.warning('请完善目的地和行程日期');
    return;
  }

  loading.value = true;
  try {
    // TODO: 调用后端 AI 规划接口
    const [startDate, endDate] = form.dates as [Date, Date];
    itineraryStore.setCurrent({
      destination: form.destination,
      startDate: startDate.toISOString(),
      endDate: endDate.toISOString(),
      budget: form.budget,
      travelers: form.travelers,
      preferences: [...form.preferences],
      dailyPlans: [],
    });
    ElMessage.success('行程规划生成成功（示例数据）');
  } catch (error) {
    ElMessage.error('生成行程时出现问题，请稍后再试');
  } finally {
    loading.value = false;
  }
};

const handleReset = () => {
  form.destination = '';
  form.dates = [];
  form.budget = 10000;
  form.travelers = 2;
  form.preferences = [];
  itineraryStore.setCurrent(null);
};

const handleVoiceInput = (content: string) => {
  if (!content) {
    return;
  }
  // TODO: 解析语音文本填充表单
  ElMessage.info(`语音识别结果：${content}`);
};
</script>

<style scoped>
.planner-view {
  display: grid;
  grid-template-columns: 420px 1fr;
  gap: 24px;
  padding: 24px;
  box-sizing: border-box;
}

.planner-view__form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow-y: auto;
}

.planner-view__result {
  overflow: hidden;
  display: flex;
}

@media (max-width: 960px) {
  .planner-view {
    grid-template-columns: 1fr;
  }
}
</style>

