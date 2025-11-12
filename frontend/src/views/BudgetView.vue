<template>
  <section class="budget-view">
    <div class="budget-view__summary panel" v-loading="loading">
      <h2>预算概览</h2>
      <el-statistic title="预计预算" :value="budgetStore.expectedBudget" unit="元" />
      <el-statistic title="已用预算" :value="budgetStore.totalExpense" unit="元" />
      <el-statistic title="剩余预算" :value="budgetStore.remainingBudget" unit="元" />
    </div>
    <div class="budget-view__list panel" v-loading="loading">
      <div class="budget-view__list-header">
        <h3>费用记录</h3>
        <el-button type="primary" @click="openDialog">新增记录</el-button>
      </div>
      <el-empty v-if="!expenses.length" description="暂无费用记录，尝试语音或手动记账" />
      <el-table v-else :data="expenses" stripe>
        <el-table-column label="时间" width="200">
          <template #default="{ row }">
            {{ dayjs(row.recordedAt).format('YYYY-MM-DD HH:mm') }}
          </template>
        </el-table-column>
        <el-table-column label="类别" width="120">
          <template #default="{ row }">
            {{ categoryLabel(row.category) }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" />
        <el-table-column prop="note" label="备注" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="danger" link @click="removeExpense(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <VoiceInputCard @confirmed="handleVoiceInput" />
    <el-dialog v-model="dialogVisible" title="新增费用">
      <el-form :model="form" label-position="top">
        <el-form-item label="类别">
          <el-select v-model="form.category">
            <el-option
              v-for="item in categories"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="form.amount" :min="0" :step="50" />
        </el-form-item>
        <el-form-item label="货币">
          <el-input v-model="form.currency" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.note" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitExpense">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';

import VoiceInputCard from '@/components/voice/VoiceInputCard.vue';
import { useBudgetStore, useItineraryStore } from '@/stores';
import type { ExpensePayload } from '@/types/expense';

const budgetStore = useBudgetStore();
const itineraryStore = useItineraryStore();

const dialogVisible = ref(false);
const form = reactive({
  category: 'TRANSPORT',
  amount: 0,
  currency: 'CNY',
  note: '',
});

const categories = [
  { value: 'TRANSPORT', label: '交通' },
  { value: 'HOTEL', label: '住宿' },
  { value: 'FOOD', label: '餐饮' },
  { value: 'ENTERTAINMENT', label: '娱乐' },
  { value: 'SHOPPING', label: '购物' },
  { value: 'OTHER', label: '其他' },
];

const currentPlan = computed(() => itineraryStore.currentPlan);
const expenses = computed(() => budgetStore.expenses);
const loading = computed(() => budgetStore.loading || itineraryStore.loading);

watch(
  currentPlan,
  async (plan) => {
    if (plan?.id) {
      budgetStore.setExpectedBudget(plan.budget);
      try {
        await budgetStore.loadExpenses(plan.id);
      } catch (error) {
        ElMessage.error((error as Error).message || '加载费用失败');
      }
    } else {
      budgetStore.reset();
    }
  },
  { immediate: true },
);

const openDialog = () => {
  if (!currentPlan.value?.id) {
    ElMessage.warning('请先生成或选择行程');
    return;
  }
  dialogVisible.value = true;
};

const submitExpense = async () => {
  if (!currentPlan.value?.id) {
    ElMessage.warning('请先生成或选择行程');
    return;
  }
  if (!form.amount) {
    ElMessage.warning('请输入金额');
    return;
  }

  const payload: ExpensePayload = {
    category: form.category as ExpensePayload['category'],
    amount: form.amount,
    currency: form.currency,
    note: form.note,
    recordedAt: dayjs().toISOString(),
  };

  try {
    await budgetStore.addExpense(currentPlan.value.id, payload);
    dialogVisible.value = false;
    Object.assign(form, { category: 'TRANSPORT', amount: 0, currency: 'CNY', note: '' });
    ElMessage.success('费用记录已保存');
  } catch (error) {
    ElMessage.error((error as Error).message || '保存费用失败');
  }
};

const removeExpense = async (id?: string) => {
  if (!id || !currentPlan.value?.id) {
    return;
  }
  try {
    await budgetStore.removeExpense(currentPlan.value.id, id);
    ElMessage.success('已删除费用记录');
  } catch (error) {
    ElMessage.error((error as Error).message || '删除费用失败');
  }
};

const categoryLabel = (value: string) => {
  return categories.find((item) => item.value === value)?.label ?? value;
};

const handleVoiceInput = (content: string) => {
  if (!content) {
    return;
  }
  // TODO: 将语音识别结果解析为费用记录
  ElMessage.info(`语音记账识别：${content}`);
};
</script>

<style scoped>
.budget-view {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  padding: 24px;
  box-sizing: border-box;
  align-items: start;
}

.budget-view__summary {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.budget-view__list {
  overflow: hidden;
}

.budget-view__list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

@media (max-width: 960px) {
  .budget-view {
    grid-template-columns: 1fr;
  }
}
</style>


