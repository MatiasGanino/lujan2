import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PeregrinosService } from '../service/peregrinos.service';
import { IPeregrinos } from '../peregrinos.model';
import { PeregrinosFormService } from './peregrinos-form.service';

import { PeregrinosUpdateComponent } from './peregrinos-update.component';

describe('Peregrinos Management Update Component', () => {
  let comp: PeregrinosUpdateComponent;
  let fixture: ComponentFixture<PeregrinosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peregrinosFormService: PeregrinosFormService;
  let peregrinosService: PeregrinosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PeregrinosUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PeregrinosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeregrinosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peregrinosFormService = TestBed.inject(PeregrinosFormService);
    peregrinosService = TestBed.inject(PeregrinosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const peregrinos: IPeregrinos = { id: 6885 };

      activatedRoute.data = of({ peregrinos });
      comp.ngOnInit();

      expect(comp.peregrinos).toEqual(peregrinos);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeregrinos>>();
      const peregrinos = { id: 22905 };
      jest.spyOn(peregrinosFormService, 'getPeregrinos').mockReturnValue(peregrinos);
      jest.spyOn(peregrinosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peregrinos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peregrinos }));
      saveSubject.complete();

      // THEN
      expect(peregrinosFormService.getPeregrinos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(peregrinosService.update).toHaveBeenCalledWith(expect.objectContaining(peregrinos));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeregrinos>>();
      const peregrinos = { id: 22905 };
      jest.spyOn(peregrinosFormService, 'getPeregrinos').mockReturnValue({ id: null });
      jest.spyOn(peregrinosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peregrinos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peregrinos }));
      saveSubject.complete();

      // THEN
      expect(peregrinosFormService.getPeregrinos).toHaveBeenCalled();
      expect(peregrinosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeregrinos>>();
      const peregrinos = { id: 22905 };
      jest.spyOn(peregrinosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peregrinos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peregrinosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
