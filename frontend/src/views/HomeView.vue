<template>
  <section class="home-view">
    <aside class="home-view__sidebar panel">
      <el-menu :default-active="activeMenu" class="home-view__menu">
        <el-menu-item index="planner" @click="goPlanner">
          立即规划旅程
        </el-menu-item>
        <el-menu-item index="budget" @click="goBudget">
          查看预算管理
        </el-menu-item>
      </el-menu>
      <VoiceInputCard />
    </aside>
    <div class="home-view__map panel">
      <MapContainer />
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';

import MapContainer from '@/components/map/MapContainer.vue';
import VoiceInputCard from '@/components/voice/VoiceInputCard.vue';

const router = useRouter();
const activeMenu = computed(() => router.currentRoute.value.name?.toString() ?? 'planner');

const goPlanner = () => router.push({ name: 'planner' });
const goBudget = () => router.push({ name: 'budget' });
</script>

<style scoped>
.home-view {
  display: grid;
  padding: 24px;
  gap: 24px;
  grid-template-columns: 360px 1fr;
  height: calc(100vh - 96px);
  box-sizing: border-box;
}

.home-view__sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.home-view__menu {
  border: none;
}

.home-view__map {
  padding: 0;
  overflow: hidden;
}

@media (max-width: 960px) {
  .home-view {
    grid-template-columns: 1fr;
    grid-template-rows: auto 1fr;
  }
}
</style>

