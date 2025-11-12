import { defineStore } from 'pinia';

import { createExpense, deleteExpense, fetchExpenses } from '@/services/budgetService';
import type { Expense, ExpensePayload } from '@/types/expense';

interface BudgetState {
  expectedBudget: number;
  expenses: Expense[];
  loading: boolean;
  currentTripId: string | null;
}

export const useBudgetStore = defineStore('budget', {
  state: (): BudgetState => ({
    expectedBudget: 0,
    expenses: [],
    loading: false,
    currentTripId: null,
  }),
  getters: {
    totalExpense: (state) => state.expenses.reduce((sum, item) => sum + item.amount, 0),
    remainingBudget(): number {
      return this.expectedBudget - this.totalExpense;
    },
  },
  actions: {
    setExpectedBudget(value: number) {
      this.expectedBudget = value;
    },
    setCurrentTrip(tripId: string | null) {
      this.currentTripId = tripId;
    },
    async loadExpenses(tripId: string) {
      this.loading = true;
      this.currentTripId = tripId;
      try {
        this.expenses = await fetchExpenses(tripId);
      } finally {
        this.loading = false;
      }
    },
    async addExpense(tripId: string, payload: ExpensePayload) {
      this.loading = true;
      try {
        const expense = await createExpense(tripId, payload);
        this.expenses.push(expense);
        return expense;
      } finally {
        this.loading = false;
      }
    },
    async removeExpense(tripId: string, id: string) {
      this.loading = true;
      try {
        await deleteExpense(tripId, id);
        this.expenses = this.expenses.filter((item) => item.id !== id);
      } finally {
        this.loading = false;
      }
    },
    reset() {
      this.expenses = [];
      this.expectedBudget = 0;
      this.currentTripId = null;
    },
  },
});


