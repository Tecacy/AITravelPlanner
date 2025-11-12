import apiClient from './api';

import type { ApiResponse } from '@/types/api';
import type { Expense, ExpensePayload } from '@/types/expense';

export const fetchExpenses = async (tripId: string) => {
  const res = await apiClient.get<ApiResponse<Expense[]>>(`/trips/${tripId}/expenses`);
  return res.data;
};

export const createExpense = async (tripId: string, payload: ExpensePayload) => {
  const res = await apiClient.post<ApiResponse<Expense>>(`/trips/${tripId}/expenses`, payload);
  return res.data;
};

export const deleteExpense = async (tripId: string, expenseId: string) => {
  const res = await apiClient.delete<ApiResponse<void>>(`/trips/${tripId}/expenses/${expenseId}`);
  return res.message ?? '删除成功';
};

