
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import PowerGenerationManager from "./components/listers/PowerGenerationCards"
import PowerGenerationDetail from "./components/listers/PowerGenerationDetail"

import SepManager from "./components/listers/SepCards"
import SepDetail from "./components/listers/SepDetail"


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
                path: '/seps',
                name: 'SepManager',
                component: SepManager
            },
            {
                path: '/seps/:id',
                name: 'SepDetail',
                component: SepDetail
            },



    ]
})
