export type ExpenseCategory =
  | 'TRANSPORT'
  | 'HOTEL'
  | 'FOOD'
  | 'ENTERTAINMENT'
  | 'SHOPPING'
  | 'OTHER';

export interface Expense {
  id?: string;
  category: ExpenseCategory;
  amount: number;
  currency: string;
  note?: string;
  recordedAt: string;
  itineraryId?: string;
}

export interface ExpensePayload {
  category: ExpenseCategory;
  amount: number;
  currency: string;
  note?: string;
  recordedAt?: string;
}

