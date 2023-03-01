import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/manage.vue'

Vue.use(VueRouter)

const routes = [
    {path: '/login', name: 'Login', component: () => import('../views/login.vue')},
    {path: '/register', name: 'Register', component: () => import('../views/registor.vue')},
    {path: '/manage', name: 'Manage', component: () => import( '../views/manage.vue'),
        // redirect: "/manage/managehome",
        children: [
            {path: 'managehome', name: 'ManageHome', component: () => import( '../views/ManageHome.vue')},
            {path: 'inventory', name: 'Inventory', component: () => import( '../views/ManageInventory.vue')},
            {path: 'selldata/records', name: 'Records', component: () => import( '../views/ManageRecords.vue')},
            {path: 'selldata/statistics', name: 'Statistics', component: () => import( '../views/ManageStatistics.vue')}
        ]
    },
    {path: '/admin', name: 'Admin', component: () => import( '../views/admin.vue'),
        // redirect: "/admin/managehome",
        children: [
            {path: 'adminhome', name: 'AdminHome', component: () => import( '../views/AdminHome.vue')},
            {path: 'admincarousel', name: 'AdminCarousel', component: () => import( '../views/AdminCarousel.vue')},
        ]
    },
    {path: '/store', name: 'Store', component: () => import( '../views/store.vue'), redirect: "/store/storemain",
        children: [
            {path: 'storemain', name: 'StoreMain', component: () => import( '../views/StoreMain.vue')},
            {path: 'storehome', name: 'StoreHome', component: () => import( '../views/StoreHome.vue')},
            {path: 'storeorders', name: 'StoreOrders', component: () => import( '../views/StoreOrders.vue')},
            {path: 'storecart', name: 'StoreCart', component: () => import( '../views/StoreCart.vue')},
            {path: 'storesettle', name: 'StoreSettle', component: () => import( '../views/StoreSettle.vue')},
            {path: 'storepay', name: 'StorePay', component: () => import( '../views/StorePay.vue')},
        ]
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPath", to.name) // 设置当前路由内容
    next()
})

export default router
