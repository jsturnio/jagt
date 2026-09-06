import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import MutualResolve from './route/mutual-routing-resolve.service';

const mutualRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/mutual').then(m => m.Mutual),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/mutual-ag').then(m => m.MutualAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/mutual-detail').then(m => m.MutualDetail),
    resolve: {
      mutual: MutualResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/mutual-update').then(m => m.MutualUpdate),
    resolve: {
      mutual: MutualResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/mutual-update').then(m => m.MutualUpdate),
    resolve: {
      mutual: MutualResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default mutualRoute;
