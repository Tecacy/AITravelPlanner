import apiClient from './api';

import type { ApiResponse } from '@/types/api';
import type { AiTripPlanRequest, AiTripPlanResponse } from '@/types/ai';

export const generateTripPlan = async (payload: AiTripPlanRequest) => {
  const res = await apiClient.post<ApiResponse<AiTripPlanResponse>>('/ai/trip-plans', payload);
  return res.data;
};

