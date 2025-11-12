export interface TripActivity {
  id?: string;
  title: string;
  time?: string;
  location?: string;
  description?: string;
  estimatedCost?: number;
}

export interface TripDay {
  id?: string;
  date: string;
  summary?: string;
  activities: TripActivity[];
}

export interface TripPlan {
  id?: string;
  destination: string;
  startDate: string;
  endDate: string;
  travelers: number;
  budget: number;
  preferences: string[];
  userId?: string;
  createdAt?: string;
  updatedAt?: string;
  dailyPlans: TripDay[];
}

export interface TripPlanPayload {
  destination: string;
  startDate: string;
  endDate: string;
  travelers: number;
  budget: number;
  preferences: string[];
  userId?: string;
  dailyPlans: TripDay[];
}

