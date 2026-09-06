import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PrestacionResolve from './route/prestacion-routing-resolve.service';

const prestacionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/prestacion').then(m => m.Prestacion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/prestacion-ag').then(m => m.PrestacionAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/prestacion-detail').then(m => m.PrestacionDetail),
    resolve: {
      prestacion: PrestacionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/prestacion-update').then(m => m.PrestacionUpdate),
    resolve: {
      prestacion: PrestacionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/prestacion-update').then(m => m.PrestacionUpdate),
    resolve: {
      prestacion: PrestacionResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default prestacionRoute;
