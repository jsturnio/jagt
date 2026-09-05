import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'UserManagements',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'Authorities',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'prestacion',
    title: 'Prestacions',
    loadChildren: () => import('./prestacion/prestacion.routes'),
  },
  {
    path: 'mutual',
    title: 'Mutuals',
    loadChildren: () => import('./mutual/mutual.routes'),
  },
  {
    path: 'plan-mutual',
    title: 'PlanMutuals',
    loadChildren: () => import('./plan-mutual/plan-mutual.routes'),
  },
  {
    path: 'paquete',
    title: 'Paquetes',
    loadChildren: () => import('./paquete/paquete.routes'),
  },
  {
    path: 'nomenclador',
    title: 'Nomencladors',
    loadChildren: () => import('./nomenclador/nomenclador.routes'),
  },
  {
    path: 'empleado',
    title: 'Empleados',
    loadChildren: () => import('./empleado/empleado.routes'),
  },
  {
    path: 'bioquimico',
    title: 'Bioquimicos',
    loadChildren: () => import('./bioquimico/bioquimico.routes'),
  },
  {
    path: 'orden',
    title: 'Ordens',
    loadChildren: () => import('./orden/orden.routes'),
  },
  {
    path: 'practica',
    title: 'Practicas',
    loadChildren: () => import('./practica/practica.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
