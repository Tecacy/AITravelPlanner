<template>
  <div class="app-shell">
    <header class="app-header">
      <div class="app-header__top">
        <div>
          <h1>AI Travel Planner</h1>
          <span class="tagline">智能旅行助手 · 规划 · 预算 · 实时辅助</span>
        </div>
        <el-button size="small" plain @click="showSettings = true">
          API Key 设置
        </el-button>
      </div>
      <small class="tagline hint">
        密钥将保存在浏览器或可选的配置文件中，请勿在公共仓库提交真实 Key。
      </small>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
    <ApiKeySettings v-model="showSettings" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';

import ApiKeySettings from '@/components/settings/ApiKeySettings.vue';
import { useSettingsStore } from '@/stores';

const showSettings = ref(false);
const settingsStore = useSettingsStore();

onMounted(() => {
  settingsStore.initialize();
});
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 24px;
  background: linear-gradient(120deg, #1677ff, #49bbed);
  color: #fff;
}

.app-header__top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.app-header h1 {
  font-size: 20px;
  margin: 0;
}

.tagline {
  opacity: 0.85;
}

.hint {
  display: block;
  opacity: 0.75;
  font-size: 12px;
}

.app-main {
  flex: 1;
  background: #f5f7fa;
  padding: 0;
}
</style>
