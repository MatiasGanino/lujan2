import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { IngresoPublicoService, IngresoFormData } from './ingreso-publico.service';

@Component({
  selector: 'jhi-autorizacion',
  standalone: true,
  templateUrl: './autorizacion.component.html',
  imports: [CommonModule],
})
export class AutorizacionComponent implements OnInit {
  formData: IngresoFormData | null = null;

  private router = inject(Router);
  private ingresoService = inject(IngresoPublicoService);

  ngOnInit(): void {
    this.formData = this.ingresoService.getFormData();
    // Si alguien entra directamente a esta URL sin pasar por el formulario, lo mandamos atrás
    if (!this.formData) {
      this.router.navigate(['/ingreso']);
    }
  }

  volver(): void {
    this.router.navigate(['/ingreso']);
  }

  aceptarYPagar(): void {
    // Los datos ya están guardados en el servicio; vamos al pago
    this.router.navigate(['/ingreso/pago']);
  }
}
