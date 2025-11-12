export interface AiTripPlanRequest {
  prompt: string;
  destination?: string;
  startDate?: string;
  endDate?: string;
  budget?: number;
  travelers?: number;
  preferences?: string[];
}

export interface AiTripPlanResponse {
  summary: string;
  plan: TripPlanSuggestion;
}

export interface TripPlanSuggestion {
  destination: string;
  startDate: string;
  endDate: string;
  travelers: number;
  budget: number;
  preferences: string[];
  dailyPlans: TripDaySuggestion[];
}

export interface TripDaySuggestion {
  date: string;
  summary: string;
  activities: TripActivitySuggestion[];
}

export interface TripActivitySuggestion {
  time?: string;
  title: string;
  location?: string;
  description?: string;
  estimatedCost?: number;
}

