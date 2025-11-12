<template>
  <el-dialog
    v-model="visible"
    title="API Key 设置"
    width="480px"
    :close-on-click-modal="false"
  >
    <el-alert
      type="info"
      :closable="false"
      class="settings-alert"
      description="所有密钥仅保存在本地浏览器或可选的配置文件中，不会上传至服务器。"
    />
    <el-form label-position="top" :model="form">
      <el-form-item label="百度地图 AK">
        <el-input
          v-model="form.baiduAk"
          placeholder="用于加载百度地图 WebGL SDK"
          autocomplete="off"
        />
        <small class="form-tip">
          可在百度开发者平台注册申请。修改后如地图未刷新，可刷新页面重新加载。
        </small>
      </el-form-item>
      <el-form-item label="行程规划 LLM API Key">
        <el-input
          v-model="form.aiApiKey"
          placeholder="例如 OpenAI、通义千问等大模型服务 Key"
          autocomplete="off"
        />
        <small class="form-tip">
          前端会在调用行程生成接口时通过自定义请求头发送，后端可读取并代理到实际 LLM。
        </small>
      </el-form-item>
      <el-form-item label="语音识别 API Key">
        <el-input
          v-model="form.voiceApiKey"
          placeholder="例如 科大讯飞 ASR Key"
          autocomplete="off"
        />
        <small class="form-tip">
          用于语音识别服务调用，前端同样以请求头方式传递给后端。
        </small>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleReset">清除本地 Key</el-button>
        <el-space>
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-space>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';

import { useSettingsStore } from '@/stores';

const props = defineProps<{
  modelValue: boolean;
}>();

const emit = defineEmits<{
  (event: 'update:modelValue', value: boolean): void;
}>();

const settingsStore = useSettingsStore();

const visible = computed({
  get: () => props.modelValue,
  set: (value: boolean) => emit('update:modelValue', value),
});

const form = ref({
  baiduAk: settingsStore.baiduAk,
  aiApiKey: settingsStore.aiApiKey,
  voiceApiKey: settingsStore.voiceApiKey,
});

watch(
  () => props.modelValue,
  (open) => {
    if (open) {
      form.value = {
        baiduAk: settingsStore.baiduAk,
        aiApiKey: settingsStore.aiApiKey,
        voiceApiKey: settingsStore.voiceApiKey,
      };
    }
  },
);

const handleSave = () => {
  settingsStore.setBaiduAk(form.value.baiduAk);
  settingsStore.setAiApiKey(form.value.aiApiKey);
  settingsStore.setVoiceApiKey(form.value.voiceApiKey);
  emit('update:modelValue', false);
};

const handleCancel = () => {
  emit('update:modelValue', false);
};

const handleReset = () => {
  settingsStore.resetKeys();
  form.value = {
    baiduAk: '',
    aiApiKey: '',
    voiceApiKey: '',
  };
};
</script>

<style scoped>
.settings-alert {
  margin-bottom: 16px;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-tip {
  display: block;
  margin-top: 4px;
  color: #94a3b8;
}
</style>

