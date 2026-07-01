import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { IngresoPublicoService, IngresoFormData } from './ingreso-publico.service';

interface MercadoPagoPreference {
  id: string;
  init_point: string;   // URL de checkout producción
  sandbox_init_point: string; // URL de checkout sandbox
}

@Component({
  selector: 'jhi-pago',
  standalone: true,
  templateUrl: './pago.component.html',
  imports: [CommonModule],
})
export class PagoComponent implements OnInit {
  formData: IngresoFormData | null = null;
  isLoading = false;
  errorMessage: string | null = null;

  private router = inject(Router);
  private http = inject(HttpClient);
  private ingresoService = inject(IngresoPublicoService);

  ngOnInit(): void {
    this.formData = this.ingresoService.getFormData();
    if (!this.formData) {
      this.router.navigate(['/ingreso']);
    }
  }

  volver(): void {
    this.router.navigate(['/ingreso/autorizacion']);
  }

  iniciarPago(): void {
    if (!this.formData) return;

    this.isLoading = true;
    this.errorMessage = null;

    // El backend crea una preferencia en MercadoPago y nos devuelve la URL de pago.
    // El endpoint recibe los datos del peregrino (aún no grabado en BD — eso ocurre
    // después del webhook de MercadoPago o en la URL de retorno exitosa).
    this.http.post<MercadoPagoPreference>('/api/public/peregrinos/iniciar-pago', this.formData)
      .subscribe({
        next: (preference) => {
          // Redirigir al checkout de MercadoPago
          // Usar sandbox_init_point en desarrollo / init_point en producción
          window.location.href = preference.init_point;
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'No pudimos conectar con el sistema de pago. Por favor, intentá de nuevo en unos momentos.';
          console.error('Error al crear preferencia de pago:', err);
        },
      });
  }
}
