import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/WelcomePage.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    meta: { title: 'Devine Codemy' },
    component: HomePage
  },
	{
    path: '/Playground:levelNumber?',
    name: 'Playground',
    params: true,
    meta: { title: 'Playground - Devine Codemy' },
    component: () => import(/* webpackChunkName: "Playground" */ '../views/PlaygroundPage.vue'),
  },

  {
    path: '/listlevels',
    name: 'Levels',
    meta: { title: 'Levels - Devine Codemy' },
    component: () => import(/* webpackChunkName: "LevelsPage" */ '../views/LevelsPage.vue'),
  },
  {
    path: '/hub',
    name: 'Hub',
    meta: { title: 'Hub - Devine Codemy' },
    component: () => import(/* webpackChunkName: "HubPage" */ '../views/HubPage'),
  },
  {
    path: '/profile',
    name: 'Profile',
    meta: { title: 'Profile - Devine Codemy' },
    component: () => import(/* webpackChunkName: "ProfilePage" */ '../views/ProfilePage.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    meta: { title: 'Login - Devine Codemy' },
    component: () => import(/* webpackChunkName: "LoginPage" */ '../views/LoginPage.vue'),
  },
  {
    path: '/error',
    name: 'Error',
    meta: { title: 'Error - Devine Codemy' },
    component: () => import(/* webpackChunkName: "ErrorPage" */ '../views/ErrorPage.vue'),
  },
  {
    path: '/leaderboard',
    name: 'Leaderboard',
    meta: { title: 'Leaderboard - Devine Codemy' },
    component: () => import(/* webpackChunkName: "LeaderboardPage" */ '../views/LeaderboardPage')
  },
  {
    path: '/about',
    name: 'About',
    meta: { title: 'About - Devine Codemy' },
    component: () => import(/* webpackChunkName: "AboutPage" */ '../views/AboutPage.vue'),
  },
  {
    path: '/team',
    name: 'Team',
    meta: { title: 'Team - Devine Codemy' },
    component: () => import(/* webpackChunkName: "TeamPage" */ '../views/TeamPage.vue'),
  },
  {
    path: '/links',
    name: 'Links',
    meta: { title: 'Useful Links - Devine Codemy' },
    component: () => import(/* webpackChunkName: "UsefulLinksPage" */ '../views/UsefulLinksPage.vue'),
  },
  {
    path: '/gameinf',
    name: 'Gameinf',
    meta: { title: 'Info - Devine Codemy' },
    component: () => import(/* webpackChunkName: "GameInformationPage" */ '../views/GameInformationPage.vue'),
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
