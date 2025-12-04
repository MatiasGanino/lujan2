import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PeregrinosDetailComponent } from './peregrinos-detail.component';

describe('Peregrinos Management Detail Component', () => {
  let comp: PeregrinosDetailComponent;
  let fixture: ComponentFixture<PeregrinosDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeregrinosDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./peregrinos-detail.component').then(m => m.PeregrinosDetailComponent),
              resolve: { peregrinos: () => of({ id: 22905 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PeregrinosDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PeregrinosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load peregrinos on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PeregrinosDetailComponent);

      // THEN
      expect(instance.peregrinos()).toEqual(expect.objectContaining({ id: 22905 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
