import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface IngresoFormData {
  apellido: string;
  nombre: string;
  numeroDocumento?: string;
  telefono?: string;
  email?: string;
  mayorMenor: string;
  salida: string;
  formaPago: string;
  aclaraciones?: string;
  contactoEmergencia?: string;
  telefonoEmergencia?: string;
}

@Injectable({ providedIn: 'root' })
export class IngresoPublicoService {
  private formData$ = new BehaviorSubject<IngresoFormData | null>(null);

  setFormData(data: IngresoFormData): void {
    this.formData$.next(data);
  }

  getFormData(): IngresoFormData | null {
    return this.formData$.getValue();
  }

  clearFormData(): void {
    this.formData$.next(null);
  }
}
