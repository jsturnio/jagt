import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import EmpleadoResolve from './route/empleado-routing-resolve.service';

const empleadoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/empleado').then(m => m.Empleado),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'list-ag',
    loadComponent: () => import('./list-ag/empleado-ag').then(m => m.EmpleadoAg),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/empleado-detail').then(m => m.EmpleadoDetail),
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/empleado-update').then(m => m.EmpleadoUpdate),
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/empleado-update').then(m => m.EmpleadoUpdate),
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default empleadoRoute;
