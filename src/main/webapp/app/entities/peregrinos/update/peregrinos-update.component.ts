import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TipoPersona } from 'app/entities/enumerations/tipo-persona.model';
import { TipoSalida } from 'app/entities/enumerations/tipo-salida.model';
import { TipoFormaPago } from 'app/entities/enumerations/tipo-forma-pago.model';
import { PeregrinosService } from '../service/peregrinos.service';
import { IPeregrinos } from '../peregrinos.model';
import { PeregrinosFormGroup, PeregrinosFormService } from './peregrinos-form.service';

@Component({
  selector: 'jhi-peregrinos-update',
  templateUrl: './peregrinos-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PeregrinosUpdateComponent implements OnInit {
  isSaving = false;
  peregrinos: IPeregrinos | null = null;
  tipoPersonaValues = Object.keys(TipoPersona);
  tipoSalidaValues = Object.keys(TipoSalida);
  tipoFormaPagoValues = Object.keys(TipoFormaPago);

  protected peregrinosService = inject(PeregrinosService);
  protected peregrinosFormService = inject(PeregrinosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PeregrinosFormGroup = this.peregrinosFormService.createPeregrinosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peregrinos }) => {
      this.peregrinos = peregrinos;
      if (peregrinos) {
        this.updateForm(peregrinos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const peregrinos = this.peregrinosFormService.getPeregrinos(this.editForm);
    if (peregrinos.id !== null) {
      this.subscribeToSaveResponse(this.peregrinosService.update(peregrinos));
    } else {
      this.subscribeToSaveResponse(this.peregrinosService.create(peregrinos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeregrinos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(peregrinos: IPeregrinos): void {
    this.peregrinos = peregrinos;
    this.peregrinosFormService.resetForm(this.editForm, peregrinos);
  }
}
