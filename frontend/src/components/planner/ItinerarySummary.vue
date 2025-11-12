<template>
  <section class="summary">
    <template v-if="loading">
      <el-skeleton :rows="6" animated />
    </template>
    <template v-else-if="!plan">
      <el-empty description="请先填写需求并生成行程" />
    </template>
    <template v-else>
      <header class="summary__header">
        <div>
          <h2>{{ plan.destination }}</h2>
          <p>{{ dateRange }}</p>
        </div>
        <div class="summary__badges">
          <el-tag type="success">预算：{{ plan.budget }} 元</el-tag>
          <el-tag>同行：{{ plan.travelers }} 人</el-tag>
        </div>
      </header>
      <el-timeline>
        <el-timeline-item
          v-for="(day, index) in plan.dailyPlans"
          :key="day.date ?? index"
          :timestamp="formatDate(day.date)"
          placement="top"
        >
          <el-card>
            <h3>{{ day.summary }}</h3>
            <ul>
              <li v-for="activity in day.activities" :key="activity.title">
                <strong>{{ activity.time }}</strong>
                <span>{{ activity.title }}</span>
                <span v-if="activity.location"> - {{ activity.location }}</span>
              </li>
            </ul>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </template>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue';

import dayjs from 'dayjs';

import type { Itinerary } from '@/stores/useItineraryStore';

const props = defineProps<{
  plan: Itinerary | null;
  loading?: boolean;
}>();

const dateRange = computed(() => {
  if (!props.plan) {
    return '';
  }
  const start = dayjs(props.plan.startDate).format('YYYY-MM-DD');
  const end = dayjs(props.plan.endDate).format('YYYY-MM-DD');
  return `${start} ~ ${end}`;
});

const formatDate = (input: string | undefined) => {
  if (!input) {
    return '未指定日期';
  }
  return dayjs(input).format('YYYY-MM-DD');
};
</script>

<style scoped>
.summary {
  width: 100%;
  overflow-y: auto;
  padding: 16px;
}

.summary__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 16px;
}

.summary__header h2 {
  margin: 0;
}

.summary__header p {
  margin: 4px 0 0;
  color: #64748b;
}

.summary__badges {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

ul {
  padding-left: 16px;
}
</style>

