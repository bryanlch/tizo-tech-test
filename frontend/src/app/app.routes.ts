import { Routes } from '@angular/router';
import { AuthGuard } from '@core/guards/auth.guard';
import { LayoutComponent } from '@core/layouts/layout.component';

export const APP_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('./features/home/home.routes').then((m) => m.HOME_ROUTES),
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then((m) => m.AUTH_ROUTES),
    pathMatch: 'full'
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.routes').then((m) => m.DASHBOARD_ROUTES)
      },
      {
        path: 'branches',
        loadChildren: () => import('./features/branches/branches.routes').then((m) => m.BRANCHES_ROUTES)
      },
      {
        path: 'products',
        loadChildren: () => import('./features/products/products.routes').then((m) => m.PRODUCTS_ROUTES)
      },
      {
        path: 'inventory',
        loadChildren: () => import('./features/inventory/inventory.routes').then((m) => m.INVENTORY_ROUTES)
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
