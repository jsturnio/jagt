import { Routes } from '@angular/router';

export const errorRoute: Routes = [
  {
    path: 'error',
    loadComponent: () => import('./error'),
    title: '¡Página de error!',
  },
  {
    path: 'accessdenied',
    loadComponent: () => import('./error'),
    data: {
      errorMessage: 'No tiene permisos para acceder a la página.',
    },
    title: '¡Página de error!',
  },
  {
    path: '404',
    loadComponent: () => import('./error'),
    data: {
      errorMessage: 'La página no existe.',
    },
    title: '¡Página de error!',
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
