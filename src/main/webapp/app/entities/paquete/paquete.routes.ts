import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PaqueteResolve from './route/paquete-routing-resolve.service';

const paqueteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/paquete').then(m => m.Paquete),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/paquete-ag').then(m => m.PaqueteAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/paquete-detail').then(m => m.PaqueteDetail),
    resolve: {
      paquete: PaqueteResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/paquete-update').then(m => m.PaqueteUpdate),
    resolve: {
      paquete: PaqueteResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/paquete-update').then(m => m.PaqueteUpdate),
    resolve: {
      paquete: PaqueteResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default paqueteRoute;
