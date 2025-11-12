import { defineStore } from 'pinia';

export interface DailyPlan {
  date: string;
  summary: string;
  activities: Array<{
    time: string;
    title: string;
    description?: string;
    location?: string;
    budget?: number;
    coordinates?: [number, number];
  }>;
}

export interface Itinerary {
  id?: string;
  destination: string;
  startDate: string;
  endDate: string;
  budget: number;
  travelers: number;
  preferences: string[];
  dailyPlans: DailyPlan[];
  createdAt?: string;
  updatedAt?: string;
}

interface ItineraryState {
  currentPlan: Itinerary | null;
  plans: Itinerary[];
  loading: boolean;
}

export const useItineraryStore = defineStore('itinerary', {
  state: (): ItineraryState => ({
    currentPlan: null,
    plans: [],
    loading: false,
  }),
  actions: {
    setCurrent(plan: Itinerary | null) {
      this.currentPlan = plan;
    },
    upsertPlan(plan: Itinerary) {
      const index = this.plans.findIndex((item) => item.id === plan.id);
      if (index >= 0) {
        this.plans.splice(index, 1, plan);
      } else {
        this.plans.push(plan);
      }
    },
  },
});


