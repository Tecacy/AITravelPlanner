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
        <el-form-item label="补充需求（可选）">
          <el-input
            v-model="customPrompt"
            type="textarea"
            :rows="3"
            placeholder="例如：希望安排一天自由活动，优先考虑亲子友好景点"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="generating" @click="handleGenerate">
            生成AI行程
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <VoiceInputCard @confirmed="handleVoiceInput" />
    </div>
    <div class="planner-view__result panel">
      <el-alert
        v-if="aiSummary"
        :title="'AI 总结'"
        type="success"
        :closable="false"
        :description="aiSummary"
        class="planner-view__alert"
      />
      <ItinerarySummary :plan="currentPlan" :loading="loading" />
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';

import ItinerarySummary from '@/components/planner/ItinerarySummary.vue';
import VoiceInputCard from '@/components/voice/VoiceInputCard.vue';
import { useBudgetStore, useItineraryStore } from '@/stores';
import { generateTripPlan } from '@/services/aiService';
import type { AiTripPlanRequest } from '@/types/ai';
import type { TripPlanPayload } from '@/types/trip';

const itineraryStore = useItineraryStore();
const budgetStore = useBudgetStore();
const route = useRoute();
const router = useRouter();

const loading = computed(() => itineraryStore.loading);
const currentPlan = computed(() => itineraryStore.currentPlan);

const form = reactive({
  destination: '',
  dates: [] as [Date, Date] | [],
  budget: 10000,
  travelers: 2,
  preferences: [] as string[],
});

const customPrompt = ref('');
const aiSummary = ref('');
const generating = ref(false);

const preferenceOptions = ['美食', '亲子', '自然风光', '历史文化', '购物', '冒险刺激', '动漫'];

const clearVoiceQuery = () => {
  if (route.query.voice !== undefined) {
    const { voice, ...rest } = route.query;
    router.replace({ path: route.path, query: rest });
  }
};

onMounted(async () => {
  await itineraryStore.loadPlans();
  if (route.query.voice && typeof route.query.voice === 'string') {
    customPrompt.value = route.query.voice;
    clearVoiceQuery();
  }
});

watch(
  () => route.query.voice,
  (value) => {
    if (typeof value === 'string' && value) {
      customPrompt.value = value;
      clearVoiceQuery();
    }
  },
);

watch(
  currentPlan,
  async (plan) => {
    if (!plan) {
      form.destination = '';
      form.dates = [];
      form.budget = 10000;
      form.travelers = 2;
      form.preferences = [];
      aiSummary.value = '';
      budgetStore.reset();
      return;
    }
    form.destination = plan.destination;
    form.dates = [new Date(plan.startDate), new Date(plan.endDate)];
    form.budget = plan.budget;
    form.travelers = plan.travelers;
    form.preferences = [...plan.preferences];
    budgetStore.setExpectedBudget(plan.budget);
    if (plan.id) {
      try {
        await budgetStore.loadExpenses(plan.id);
      } catch (error) {
        ElMessage.error((error as Error).message || '加载费用记录失败');
      }
    }
  },
  { immediate: true },
);

const buildPrompt = () => {
  const lines: string[] = [];
  lines.push(`Destination: ${form.destination}`);
  if (form.dates.length === 2) {
    const [startDate, endDate] = form.dates as [Date, Date];
    lines.push(`Dates: ${dayjs(startDate).format('YYYY-MM-DD')} to ${dayjs(endDate).format('YYYY-MM-DD')}`);
  }
  lines.push(`Budget: ${form.budget}`);
  lines.push(`Travelers: ${form.travelers}`);
  if (form.preferences.length) {
    lines.push(`Preferences: ${form.preferences.join(', ')}`);
  }
  if (customPrompt.value) {
    lines.push(`Additional Instructions: ${customPrompt.value}`);
  }
  return lines.join('\n');
};

const handleGenerate = async () => {
  if (!form.destination || form.dates.length !== 2) {
    ElMessage.warning('请完善目的地和行程日期');
    return;
  }
  const [startDate, endDate] = form.dates as [Date, Date];
  if (dayjs(endDate).isBefore(dayjs(startDate), 'day')) {
    ElMessage.warning('结束日期不能早于开始日期');
    return;
  }

  const start = dayjs(startDate).format('YYYY-MM-DD');
  const end = dayjs(endDate).format('YYYY-MM-DD');

  const aiRequest: AiTripPlanRequest = {
    prompt: buildPrompt(),
    destination: form.destination,
    startDate: start,
    endDate: end,
    budget: form.budget,
    travelers: form.travelers,
    preferences: [...form.preferences],
  };

  generating.value = true;
  try {
    const aiResult = await generateTripPlan(aiRequest);
    aiSummary.value = aiResult.summary;
    const suggestion = aiResult.plan;

    const payload: TripPlanPayload = {
      destination: suggestion.destination,
      startDate: suggestion.startDate,
      endDate: suggestion.endDate,
      budget: suggestion.budget,
      travelers: suggestion.travelers,
      preferences: suggestion.preferences ?? [],
      dailyPlans: (suggestion.dailyPlans ?? []).map((day) => ({
        date: day.date,
        summary: day.summary,
        activities: (day.activities ?? []).map((activity) => ({
          time: activity.time ?? '',
          title: activity.title,
          location: activity.location,
          description: activity.description,
          estimatedCost: activity.estimatedCost,
        })),
      })),
    };

    const plan = await itineraryStore.createPlan(payload);
    budgetStore.setExpectedBudget(plan.budget);
    if (plan.id) {
      await budgetStore.loadExpenses(plan.id);
    }
    ElMessage.success('AI 行程已生成并保存');
  } catch (error) {
    ElMessage.error((error as Error).message || '生成行程时出现问题，请稍后再试');
  } finally {
    generating.value = false;
  }
};

const handleReset = () => {
  form.destination = '';
  form.dates = [];
  form.budget = 10000;
  form.travelers = 2;
  form.preferences = [];
  customPrompt.value = '';
  aiSummary.value = '';
  itineraryStore.setCurrent(null);
  budgetStore.reset();
};

const handleVoiceInput = (content: string) => {
  if (!content) {
    return;
  }
  customPrompt.value = content;
  ElMessage.success('已根据语音填入补充需求');
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
  flex-direction: column;
  gap: 16px;
}

.planner-view__alert {
  flex-shrink: 0;
}

@media (max-width: 960px) {
  .planner-view {
    grid-template-columns: 1fr;
  }
}
</style>

