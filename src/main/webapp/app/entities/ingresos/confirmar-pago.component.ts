import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { IngresoPublicoService } from '../ingresos/ingreso-publico.service';

type EstadoPago = 'verificando' | 'approved' | 'pending' | 'failure' | 'error';

@Component({
  selector: 'jhi-confirmar-pago',
  standalone: true,
  templateUrl: './confirmar-pago.component.html',
  imports: [CommonModule],
})
export class ConfirmarPagoComponent implements OnInit {
  estado: EstadoPago = 'verificando';
  paymentId: string | null = null;
  errorDetalle: string | null = null;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);
  private ingresoService = inject(IngresoPublicoService);

  ngOnInit(): void {
    const params = this.route.snapshot.queryParamMap;
    const status = params.get('status');
    const paymentId = params.get('payment_id');
    const extRef = params.get('external_reference');

    this.paymentId = paymentId;

    // Si MP ya informó fallo o pendiente, no hace falta verificar contra el backend
    if (status === 'failure') {
      this.estado = 'failure';
      return;
    }

    if (status === 'pending') {
      this.estado = 'pending';
      return;
    }

    // status === 'approved' → verificamos contra el backend (nunca confiamos solo en la URL)
    if (status === 'approved' && paymentId) {
      this.verificarPago(paymentId, extRef);
    } else {
      this.estado = 'error';
      this.errorDetalle = 'No se recibieron los parámetros de pago esperados.';
    }
  }

  private verificarPago(paymentId: string, extRef: string | null): void {
    this.estado = 'verificando';

    let url = `/api/public/peregrinos/confirmar?payment_id=${paymentId}&status=approved`;
    if (extRef) url += `&external_reference=${encodeURIComponent(extRef)}`;

    this.http.get<{ resultado: string }>(url).subscribe({
      next: res => {
        this.ingresoService.clearFormData(); // limpiamos datos en memoria
        this.estado = res.resultado === 'approved' ? 'approved' : (res.resultado as EstadoPago);
      },
      error: err => {
        this.estado = 'error';
        this.errorDetalle = err?.error?.error ?? 'Error inesperado al verificar el pago.';
      },
    });
  }

  volverAlInicio(): void {
    this.router.navigate(['/ingreso']);
  }
}
