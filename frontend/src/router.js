
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import PowerGenerationManager from "./components/listers/PowerGenerationCards"
import PowerGenerationDetail from "./components/listers/PowerGenerationDetail"

import MeteringManager from "./components/listers/MeteringCards"
import MeteringDetail from "./components/listers/MeteringDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/powerGenerations',
                name: 'PowerGenerationManager',
                component: PowerGenerationManager
            },
            {
                path: '/powerGenerations/:id',
                name: 'PowerGenerationDetail',
                component: PowerGenerationDetail
            },

            {
                path: '/meterings',
                name: 'MeteringManager',
                component: MeteringManager
            },
            {
                path: '/meterings/:id',
                name: 'MeteringDetail',
                component: MeteringDetail
            },



    ]
})
