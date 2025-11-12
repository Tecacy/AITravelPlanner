import apiClient from './api';

import type { ApiResponse } from '@/types/api';
import type { TripPlan, TripPlanPayload } from '@/types/trip';

export const fetchTripPlans = async (userId?: string) => {
  const params = userId ? { userId } : undefined;
  const res = await apiClient.get<ApiResponse<TripPlan[]>>('/trips', { params });
  return res.data;
};

export const createTripPlan = async (payload: TripPlanPayload) => {
  const res = await apiClient.post<ApiResponse<TripPlan>>('/trips', payload);
  return res.data;
};

export const updateTripPlan = async (id: string, payload: TripPlanPayload) => {
  const res = await apiClient.put<ApiResponse<TripPlan>>(`/trips/${id}`, payload);
  return res.data;
};

export const deleteTripPlan = async (id: string) => {
  const res = await apiClient.delete<ApiResponse<void>>(`/trips/${id}`);
  return res.message;
};

