import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OrdenResolve from './route/orden-routing-resolve.service';

const ordenRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/orden').then(m => m.Orden),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag/orden-practica-detail',
    loadComponent: () => import('./list-ag/orden-practica-detail').then(m => m.OrdenMasterDetail),
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/orden-ag').then(m => m.OrdenAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/orden-detail').then(m => m.OrdenDetail),
    resolve: {
      orden: OrdenResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/orden-update').then(m => m.OrdenUpdate),
    resolve: {
      orden: OrdenResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/orden-update').then(m => m.OrdenUpdate),
    resolve: {
      orden: OrdenResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default ordenRoute;
