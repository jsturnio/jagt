import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import NomencladorResolve from './route/nomenclador-routing-resolve.service';

const nomencladorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/nomenclador').then(m => m.Nomenclador),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/nomenclador-ag').then(m => m.NomencladorAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/nomenclador-detail').then(m => m.NomencladorDetail),
    resolve: {
      nomenclador: NomencladorResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/nomenclador-update').then(m => m.NomencladorUpdate),
    resolve: {
      nomenclador: NomencladorResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/nomenclador-update').then(m => m.NomencladorUpdate),
    resolve: {
      nomenclador: NomencladorResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default nomencladorRoute;
