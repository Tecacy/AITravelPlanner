import { defineStore } from 'pinia';

const STORAGE_KEY = 'ai-travel-planner.apiKeys';

interface SettingsState {
  baiduAk: string;
  aiApiKey: string;
  voiceApiKey: string;
  initialized: boolean;
}

interface ApiKeyConfig {
  baiduAk?: string;
  aiApiKey?: string;
  voiceApiKey?: string;
}

function loadPersistedState(): Omit<SettingsState, 'initialized'> {
  if (typeof window === 'undefined') {
    return {
      baiduAk: '',
      aiApiKey: '',
      voiceApiKey: '',
    };
  }
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return {
        baiduAk: '',
        aiApiKey: '',
        voiceApiKey: '',
      };
    }
    const parsed = JSON.parse(raw) as ApiKeyConfig;
    return {
      baiduAk: parsed.baiduAk ?? '',
      aiApiKey: parsed.aiApiKey ?? '',
      voiceApiKey: parsed.voiceApiKey ?? '',
    };
  } catch {
    return {
      baiduAk: '',
      aiApiKey: '',
      voiceApiKey: '',
    };
  }
}

export const useSettingsStore = defineStore('settings', {
  state: (): SettingsState => ({
    ...loadPersistedState(),
    initialized: false,
  }),
  actions: {
    setBaiduAk(value: string) {
      this.baiduAk = value.trim();
      this.persist();
    },
    setAiApiKey(value: string) {
      this.aiApiKey = value.trim();
      this.persist();
    },
    setVoiceApiKey(value: string) {
      this.voiceApiKey = value.trim();
      this.persist();
    },
    resetKeys() {
      this.baiduAk = '';
      this.aiApiKey = '';
      this.voiceApiKey = '';
      this.persist();
    },
    persist() {
      if (typeof window === 'undefined') {
        return;
      }
      const payload: ApiKeyConfig = {
        baiduAk: this.baiduAk || undefined,
        aiApiKey: this.aiApiKey || undefined,
        voiceApiKey: this.voiceApiKey || undefined,
      };
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(payload));
    },
    applyConfig(config: ApiKeyConfig) {
      if (!config) {
        return;
      }
      if (!this.baiduAk && config.baiduAk) {
        this.baiduAk = config.baiduAk;
      }
      if (!this.aiApiKey && config.aiApiKey) {
        this.aiApiKey = config.aiApiKey;
      }
      if (!this.voiceApiKey && config.voiceApiKey) {
        this.voiceApiKey = config.voiceApiKey;
      }
      this.persist();
    },
    async initialize() {
      if (this.initialized) {
        return;
      }
      this.initialized = true;
      if (typeof window === 'undefined') {
        return;
      }
      try {
        const response = await fetch('/config/api-keys.json', { cache: 'no-store' });
        if (!response.ok) {
          return;
        }
        const data = (await response.json()) as ApiKeyConfig;
        this.applyConfig(data);
      } catch {
        // 忽略配置文件缺失或格式错误
      }
    },
  },
});

