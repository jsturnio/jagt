import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PlanMutualResolve from './route/plan-mutual-routing-resolve.service';

const planMutualRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/plan-mutual').then(m => m.PlanMutual),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/plan-mutual-ag').then(m => m.PlanMutualAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/plan-mutual-detail').then(m => m.PlanMutualDetail),
    resolve: {
      planMutual: PlanMutualResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/plan-mutual-update').then(m => m.PlanMutualUpdate),
    resolve: {
      planMutual: PlanMutualResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/plan-mutual-update').then(m => m.PlanMutualUpdate),
    resolve: {
      planMutual: PlanMutualResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default planMutualRoute;
