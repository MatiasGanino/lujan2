import { Routes } from '@angular/router';

const ingresoRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./ingreso.component').then(m => m.IngresoComponent),
  },
  {
    path: 'autorizacion',
    loadComponent: () => import('./autorizacion.component').then(m => m.AutorizacionComponent),
  },
  {
    path: 'pago',
    loadComponent: () => import('./pago.component').then(m => m.PagoComponent),
  },
  {
    path: 'confirmacion', // ← MercadoPago redirige acá con ?payment_id=X&status=approved
    loadComponent: () => import('./confirmar-pago.component').then(m => m.ConfirmarPagoComponent),
  },
];

export default ingresoRoutes;
