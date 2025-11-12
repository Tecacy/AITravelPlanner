<template>
  <div class="app-shell">
    <header class="app-header">
      <h1>AI Travel Planner</h1>
      <span class="tagline">智能旅行助手 · 规划 · 预算 · 实时辅助</span>
    </header>
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
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

.app-header h1 {
  font-size: 20px;
  margin: 0;
}

.tagline {
  opacity: 0.85;
}

.app-main {
  flex: 1;
  background: #f5f7fa;
  padding: 0;
}
</style>


