<template>
  <section class="budget-view">
    <div class="budget-view__summary panel">
      <h2>预算概览</h2>
      <el-statistic title="预计预算" :value="budgetStore.expectedBudget" unit="元" />
      <el-statistic title="已用预算" :value="budgetStore.totalExpense" unit="元" />
      <el-statistic title="剩余预算" :value="budgetStore.remainingBudget" unit="元" />
    </div>
    <div class="budget-view__list panel">
      <div class="budget-view__list-header">
        <h3>费用记录</h3>
        <el-button type="primary" @click="openDialog">新增记录</el-button>
      </div>
      <el-empty v-if="!budgetStore.expenses.length" description="暂无费用记录，尝试语音或手动记账" />
      <el-table v-else :data="budgetStore.expenses" stripe>
        <el-table-column prop="recordedAt" label="时间" width="180" />
        <el-table-column prop="category" label="类别" width="120" />
        <el-table-column prop="amount" label="金额" width="120" />
        <el-table-column prop="note" label="备注" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="danger" link @click="removeExpense(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <VoiceInputCard @transcribed="handleVoiceInput" />
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
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

import VoiceInputCard from '@/components/voice/VoiceInputCard.vue';
import { useBudgetStore } from '@/stores';

const budgetStore = useBudgetStore();
const dialogVisible = ref(false);
const form = reactive({
  category: 'transport',
  amount: 0,
  currency: 'CNY',
  note: '',
});

const categories = [
  { value: 'transport', label: '交通' },
  { value: 'hotel', label: '住宿' },
  { value: 'food', label: '餐饮' },
  { value: 'entertainment', label: '娱乐' },
  { value: 'shopping', label: '购物' },
  { value: 'other', label: '其他' },
];

const openDialog = () => {
  dialogVisible.value = true;
};

const submitExpense = () => {
  if (!form.amount) {
    ElMessage.warning('请输入金额');
    return;
  }

  budgetStore.addExpense({
    ...form,
    id: crypto.randomUUID(),
    recordedAt: new Date().toISOString(),
  });
  dialogVisible.value = false;
  ElMessage.success('费用记录已保存（示例数据）');
};

const removeExpense = (id?: string) => {
  if (!id) {
    return;
  }
  budgetStore.removeExpense(id);
  ElMessage.success('已删除费用记录');
};

const handleVoiceInput = (content: string) => {
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


