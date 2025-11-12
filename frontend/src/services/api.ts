import axios from 'axios';

import { useSettingsStore } from '@/stores';

const apiClient = axios.create({
  baseURL: '/api',
  timeout: 15000,
});

apiClient.interceptors.request.use((config) => {
  const settingsStore = useSettingsStore();
  config.headers = config.headers ?? {};
  if (settingsStore.aiApiKey) {
    config.headers['X-AI-API-Key'] = settingsStore.aiApiKey;
  }
  if (settingsStore.voiceApiKey) {
    config.headers['X-Voice-API-Key'] = settingsStore.voiceApiKey;
  }
  if (settingsStore.baiduAk) {
    config.headers['X-Baidu-AK'] = settingsStore.baiduAk;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const message: string = error.response?.data?.message ?? error.message ?? '请求失败';
    return Promise.reject(new Error(message));
  },
);

export default apiClient;
