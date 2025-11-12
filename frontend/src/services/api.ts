import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api',
  timeout: 15000,
});

apiClient.interceptors.request.use((config) => {
  // TODO: 附加 Supabase/Firebase 等认证 token
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error);
  },
);

export default apiClient;


