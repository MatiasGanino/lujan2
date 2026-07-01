import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { TipoPersona } from 'app/entities/enumerations/tipo-persona.model';
import { TipoSalida } from 'app/entities/enumerations/tipo-salida.model';
import { TipoFormaPago } from 'app/entities/enumerations/tipo-forma-pago.model';
import { IngresoPublicoService } from './ingreso-publico.service';

@Component({
  selector: 'jhi-ingreso',
  standalone: true,
  templateUrl: './ingreso.component.html',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
})
export class IngresoComponent implements OnInit {
  ingresoForm!: FormGroup;
  tipoPersonaValues = Object.keys(TipoPersona);
  tipoSalidaValues = Object.keys(TipoSalida);
  tipoFormaPagoValues = Object.keys(TipoFormaPago);
  currentStep = 1;
  totalSteps = 2; // form has 2 visual sections

  private fb = inject(FormBuilder);
  private router = inject(Router);
  private ingresoService = inject(IngresoPublicoService);

  ngOnInit(): void {
    this.ingresoForm = this.fb.group({
      // Datos personales
      apellido: ['', [Validators.required, Validators.maxLength(255)]],
      nombre: ['', [Validators.required, Validators.maxLength(255)]],
      numeroDocumento: ['', Validators.maxLength(255)],
      telefono: ['', Validators.maxLength(255)],
      email: ['', [Validators.email, Validators.maxLength(255)]],

      // Datos de la peregrinación
      mayorMenor: [null, Validators.required],
      salida: [null, Validators.required],
      formaPago: [null, Validators.required],
      aclaraciones: ['', Validators.maxLength(255)],

      // Contacto de emergencia
      contactoEmergencia: ['', Validators.maxLength(255)],
      telefonoEmergencia: ['', Validators.maxLength(255)],
    });
  }

  get f() {
    return this.ingresoForm.controls;
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.ingresoForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  hasError(fieldName: string, errorType: string): boolean {
    return !!(this.ingresoForm.get(fieldName)?.errors?.[errorType]);
  }

  submit(): void {
    if (this.ingresoForm.invalid) {
      this.ingresoForm.markAllAsTouched();
      return;
    }

    this.ingresoService.setFormData(this.ingresoForm.value);
    this.router.navigate(['/ingreso/autorizacion']);
  }
}
