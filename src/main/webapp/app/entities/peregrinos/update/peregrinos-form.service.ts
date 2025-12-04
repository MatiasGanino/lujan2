import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPeregrinos, NewPeregrinos } from '../peregrinos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPeregrinos for edit and NewPeregrinosFormGroupInput for create.
 */
type PeregrinosFormGroupInput = IPeregrinos | PartialWithRequiredKeyOf<NewPeregrinos>;

type PeregrinosFormDefaults = Pick<NewPeregrinos, 'id' | 'completoFormulario'>;

type PeregrinosFormGroupContent = {
  id: FormControl<IPeregrinos['id'] | NewPeregrinos['id']>;
  numeroEspecial: FormControl<IPeregrinos['numeroEspecial']>;
  apellido: FormControl<IPeregrinos['apellido']>;
  nombre: FormControl<IPeregrinos['nombre']>;
  numeroDocumento: FormControl<IPeregrinos['numeroDocumento']>;
  telefono: FormControl<IPeregrinos['telefono']>;
  mayorMenor: FormControl<IPeregrinos['mayorMenor']>;
  salida: FormControl<IPeregrinos['salida']>;
  pago: FormControl<IPeregrinos['pago']>;
  formaPago: FormControl<IPeregrinos['formaPago']>;
  aclaraciones: FormControl<IPeregrinos['aclaraciones']>;
  completoFormulario: FormControl<IPeregrinos['completoFormulario']>;
};

export type PeregrinosFormGroup = FormGroup<PeregrinosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PeregrinosFormService {
  createPeregrinosFormGroup(peregrinos: PeregrinosFormGroupInput = { id: null }): PeregrinosFormGroup {
    const peregrinosRawValue = {
      ...this.getFormDefaults(),
      ...peregrinos,
    };
    return new FormGroup<PeregrinosFormGroupContent>({
      id: new FormControl(
        { value: peregrinosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numeroEspecial: new FormControl(peregrinosRawValue.numeroEspecial),
      apellido: new FormControl(peregrinosRawValue.apellido, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(peregrinosRawValue.nombre, {
        validators: [Validators.required],
      }),
      numeroDocumento: new FormControl(peregrinosRawValue.numeroDocumento),
      telefono: new FormControl(peregrinosRawValue.telefono),
      mayorMenor: new FormControl(peregrinosRawValue.mayorMenor, {
        validators: [Validators.required],
      }),
      salida: new FormControl(peregrinosRawValue.salida, {
        validators: [Validators.required],
      }),
      pago: new FormControl(peregrinosRawValue.pago),
      formaPago: new FormControl(peregrinosRawValue.formaPago, {
        validators: [Validators.required],
      }),
      aclaraciones: new FormControl(peregrinosRawValue.aclaraciones),
      completoFormulario: new FormControl(peregrinosRawValue.completoFormulario),
    });
  }

  getPeregrinos(form: PeregrinosFormGroup): IPeregrinos | NewPeregrinos {
    return form.getRawValue() as IPeregrinos | NewPeregrinos;
  }

  resetForm(form: PeregrinosFormGroup, peregrinos: PeregrinosFormGroupInput): void {
    const peregrinosRawValue = { ...this.getFormDefaults(), ...peregrinos };
    form.reset(
      {
        ...peregrinosRawValue,
        id: { value: peregrinosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PeregrinosFormDefaults {
    return {
      id: null,
      completoFormulario: false,
    };
  }
}
