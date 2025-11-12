<template>
  <div ref="mapRef" class="map-container">
    <div v-if="loading" class="map-container__loading">
      <el-icon class="is-loading">
        <Loading />
      </el-icon>
      <span>地图加载中...</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';
import { onMounted, onUnmounted, ref } from 'vue';

import { Loading } from '@element-plus/icons-vue';
import { loadMap } from '@/services/mapLoader';

const mapRef = ref<HTMLDivElement | null>(null);
const loading = ref(true);
let mapInstance: any = null;

onMounted(async () => {
  try {
    const AMap = await loadMap();
    if (!mapRef.value) {
      return;
    }
    mapInstance = new AMap.Map(mapRef.value, {
      resizeEnable: true,
      zoom: 11,
      viewMode: '3D',
    });
  } catch (error) {
    ElMessage.error('地图加载失败，请检查 Key 配置');
  } finally {
    loading.value = false;
  }
});

onUnmounted(() => {
  if (mapInstance) {
    mapInstance.destroy();
    mapInstance = null;
  }
});
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 100%;
  min-height: 480px;
}

.map-container__loading {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(4px);
  color: #1677ff;
}
</style>

