import { defineStore } from 'pinia';

import { createTripPlan, fetchTripPlans, updateTripPlan } from '@/services/tripService';
import type { TripPlan, TripPlanPayload } from '@/types/trip';

interface ItineraryState {
  currentPlan: TripPlan | null;
  plans: TripPlan[];
  loading: boolean;
}

export const useItineraryStore = defineStore('itinerary', {
  state: (): ItineraryState => ({
    currentPlan: null,
    plans: [],
    loading: false,
  }),
  actions: {
    setCurrent(plan: TripPlan | null) {
      this.currentPlan = plan;
    },
    upsertPlan(plan: TripPlan) {
      const index = this.plans.findIndex((item) => item.id === plan.id);
      if (index >= 0) {
        this.plans.splice(index, 1, plan);
      } else {
        this.plans.push(plan);
      }
    },
    async loadPlans(userId?: string) {
      this.loading = true;
      try {
        const data = await fetchTripPlans(userId);
        this.plans = data;
        if (!this.currentPlan && data.length) {
          this.currentPlan = data[0];
        }
      } finally {
        this.loading = false;
      }
    },
    async createPlan(payload: TripPlanPayload) {
      this.loading = true;
      try {
        const plan = await createTripPlan(payload);
        this.upsertPlan(plan);
        this.currentPlan = plan;
        return plan;
      } finally {
        this.loading = false;
      }
    },
    async updatePlan(id: string, payload: TripPlanPayload) {
      this.loading = true;
      try {
        const plan = await updateTripPlan(id, payload);
        this.upsertPlan(plan);
        if (this.currentPlan?.id === id) {
          this.currentPlan = plan;
        }
        return plan;
      } finally {
        this.loading = false;
      }
    },
  },
});


