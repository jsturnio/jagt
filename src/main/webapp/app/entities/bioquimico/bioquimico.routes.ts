import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import BioquimicoResolve from './route/bioquimico-routing-resolve.service';

const bioquimicoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bioquimico').then(m => m.Bioquimico),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/bioquimico-ag').then(m => m.BioquimicoAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bioquimico-detail').then(m => m.BioquimicoDetail),
    resolve: {
      bioquimico: BioquimicoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bioquimico-update').then(m => m.BioquimicoUpdate),
    resolve: {
      bioquimico: BioquimicoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bioquimico-update').then(m => m.BioquimicoUpdate),
    resolve: {
      bioquimico: BioquimicoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default bioquimicoRoute;
