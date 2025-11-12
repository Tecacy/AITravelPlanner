<template>
  <div ref="mapRef" class="map-container">
    <div v-if="loading" class="map-container__loading">
      <el-icon class="is-loading">
        <Loading />
      </el-icon>
      <span>地图加载中...</span>
    </div>
    <div class="map-container__actions">
      <el-button type="primary" size="small" @click="handleOpenWebMap">
        在百度地图中查看
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';
import { onMounted, onUnmounted, ref, watch } from 'vue';
import { storeToRefs } from 'pinia';

import { Loading } from '@element-plus/icons-vue';
import { openBaiduMarker } from '@/services/baiduMarker';
import { loadMap } from '@/services/mapLoader';
import { useSettingsStore } from '@/stores';

const props = defineProps({
  latitude: {
    type: Number,
    default: 39.915, // 北京市中心
  },
  longitude: {
    type: Number,
    default: 116.404,
  },
  markerTitle: {
    type: String,
    default: 'AI Travel Planner 推荐地点',
  },
  markerContent: {
    type: String,
    default: '点击打开百度地图查看更多详情',
  },
});

const mapRef = ref<HTMLDivElement | null>(null);
const loading = ref(true);
let mapInstance: any = null;
let markerInstance: any = null;
let infoWindowInstance: any = null;
let BMapRef: any = null;
const settingsStore = useSettingsStore();
const { baiduAk } = storeToRefs(settingsStore);

const setupMarker = () => {
  if (!BMapRef || !mapInstance) {
    return;
  }
  const point = new BMapRef.Point(props.longitude, props.latitude);
  mapInstance.centerAndZoom(point, 13);

  markerInstance = new BMapRef.Marker(point);
  mapInstance.addOverlay(markerInstance);

  if (BMapRef.Label) {
    const label = new BMapRef.Label(props.markerTitle, {
      position: point,
      offset: new BMapRef.Size(10, -20),
    });
    label.setStyle({
      backgroundColor: '#1677ff',
      color: '#fff',
      padding: '4px 8px',
      borderRadius: '4px',
      border: 'none',
      fontSize: '12px',
    });
    markerInstance.setLabel(label);
  }

  if (BMapRef.InfoWindow) {
    infoWindowInstance = new BMapRef.InfoWindow(
      `<div><strong>${props.markerTitle}</strong><div style="margin-top:4px;">${props.markerContent}</div></div>`,
      {
        width: 260,
        height: 80,
      },
    );
    infoWindowInstance.setTitle(props.markerTitle);
    mapInstance.openInfoWindow(infoWindowInstance, point);
    markerInstance.addEventListener('click', () => {
      mapInstance.openInfoWindow(infoWindowInstance, point);
    });
  }
};

const initMap = async () => {
  try {
    if (mapRef.value) {
      mapRef.value.innerHTML = '';
    }
    BMapRef = await loadMap();
    if (!mapRef.value) {
      return;
    }
    mapInstance = new BMapRef.Map(mapRef.value);
    mapInstance.enableScrollWheelZoom(true);
    try {
      mapInstance.addControl(new BMapRef.NavigationControl());
      mapInstance.addControl(new BMapRef.ScaleControl());
    } catch {
      /* ignore */
    }
    setupMarker();
  } catch (error) {
    ElMessage.error((error as Error).message || '地图加载失败，请检查 Key 配置');
  } finally {
    loading.value = false;
  }
};

const handleOpenWebMap = () => {
  openBaiduMarker({
    latitude: props.latitude,
    longitude: props.longitude,
    title: props.markerTitle,
    content: props.markerContent,
  });
};

watch(
  () => [props.latitude, props.longitude, props.markerTitle, props.markerContent],
  () => {
    if (markerInstance) {
      mapInstance.removeOverlay?.(markerInstance);
      markerInstance = null;
    }
    setupMarker();
  },
);

watch(
  baiduAk,
  async () => {
    loading.value = true;
    mapInstance = null;
    markerInstance = null;
    infoWindowInstance = null;
    BMapRef = null;
    await initMap();
  },
);

onMounted(initMap);
onUnmounted(() => {
  mapInstance = null;
  markerInstance = null;
  infoWindowInstance = null;
  BMapRef = null;
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

.map-container__actions {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  gap: 8px;
}
</style>

