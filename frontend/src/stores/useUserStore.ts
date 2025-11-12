import { defineStore } from 'pinia';

interface UserProfile {
  id: string;
  email: string;
  displayName: string;
  preferences?: Record<string, unknown>;
}

interface UserState {
  profile: UserProfile | null;
  token: string | null;
  loading: boolean;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    profile: null,
    token: null,
    loading: false,
  }),
  actions: {
    setAuth(token: string, profile: UserProfile) {
      this.token = token;
      this.profile = profile;
    },
    clearAuth() {
      this.token = null;
      this.profile = null;
    },
  },
});


