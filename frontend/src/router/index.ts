import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue'),
    },
    {
      path: '/planner',
      name: 'planner',
      component: () => import('@/views/PlannerView.vue'),
    },
    {
      path: '/budget',
      name: 'budget',
      component: () => import('@/views/BudgetView.vue'),
    },
  ],
});

export default router;


