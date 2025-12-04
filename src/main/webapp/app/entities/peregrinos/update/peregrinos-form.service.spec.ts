import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../peregrinos.test-samples';

import { PeregrinosFormService } from './peregrinos-form.service';

describe('Peregrinos Form Service', () => {
  let service: PeregrinosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PeregrinosFormService);
  });

  describe('Service methods', () => {
    describe('createPeregrinosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPeregrinosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroEspecial: expect.any(Object),
            apellido: expect.any(Object),
            nombre: expect.any(Object),
            numeroDocumento: expect.any(Object),
            telefono: expect.any(Object),
            mayorMenor: expect.any(Object),
            salida: expect.any(Object),
            pago: expect.any(Object),
            formaPago: expect.any(Object),
            aclaraciones: expect.any(Object),
            completoFormulario: expect.any(Object),
          }),
        );
      });

      it('passing IPeregrinos should create a new form with FormGroup', () => {
        const formGroup = service.createPeregrinosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroEspecial: expect.any(Object),
            apellido: expect.any(Object),
            nombre: expect.any(Object),
            numeroDocumento: expect.any(Object),
            telefono: expect.any(Object),
            mayorMenor: expect.any(Object),
            salida: expect.any(Object),
            pago: expect.any(Object),
            formaPago: expect.any(Object),
            aclaraciones: expect.any(Object),
            completoFormulario: expect.any(Object),
          }),
        );
      });
    });

    describe('getPeregrinos', () => {
      it('should return NewPeregrinos for default Peregrinos initial value', () => {
        const formGroup = service.createPeregrinosFormGroup(sampleWithNewData);

        const peregrinos = service.getPeregrinos(formGroup) as any;

        expect(peregrinos).toMatchObject(sampleWithNewData);
      });

      it('should return NewPeregrinos for empty Peregrinos initial value', () => {
        const formGroup = service.createPeregrinosFormGroup();

        const peregrinos = service.getPeregrinos(formGroup) as any;

        expect(peregrinos).toMatchObject({});
      });

      it('should return IPeregrinos', () => {
        const formGroup = service.createPeregrinosFormGroup(sampleWithRequiredData);

        const peregrinos = service.getPeregrinos(formGroup) as any;

        expect(peregrinos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPeregrinos should not enable id FormControl', () => {
        const formGroup = service.createPeregrinosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPeregrinos should disable id FormControl', () => {
        const formGroup = service.createPeregrinosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
