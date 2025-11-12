import { defineStore } from 'pinia';

export interface ExpenseRecord {
  id?: string;
  category: 'transport' | 'hotel' | 'food' | 'entertainment' | 'shopping' | 'other';
  amount: number;
  currency: string;
  note?: string;
  recordedAt: string;
  itineraryId?: string;
}

interface BudgetState {
  expectedBudget: number;
  expenses: ExpenseRecord[];
}

export const useBudgetStore = defineStore('budget', {
  state: (): BudgetState => ({
    expectedBudget: 0,
    expenses: [],
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
    addExpense(expense: ExpenseRecord) {
      this.expenses.push(expense);
    },
    removeExpense(id: string) {
      this.expenses = this.expenses.filter((item) => item.id !== id);
    },
  },
});


