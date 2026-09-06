import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PracticaResolve from './route/practica-routing-resolve.service';

const practicaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/practica').then(m => m.Practica),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/practica-ag').then(m => m.PracticaAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/practica-detail').then(m => m.PracticaDetail),
    resolve: {
      practica: PracticaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/practica-update').then(m => m.PracticaUpdate),
    resolve: {
      practica: PracticaResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/practica-update').then(m => m.PracticaUpdate),
    resolve: {
      practica: PracticaResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default practicaRoute;
