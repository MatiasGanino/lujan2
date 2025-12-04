import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPeregrinos } from '../peregrinos.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../peregrinos.test-samples';

import { PeregrinosService } from './peregrinos.service';

const requireRestSample: IPeregrinos = {
  ...sampleWithRequiredData,
};

describe('Peregrinos Service', () => {
  let service: PeregrinosService;
  let httpMock: HttpTestingController;
  let expectedResult: IPeregrinos | IPeregrinos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PeregrinosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Peregrinos', () => {
      const peregrinos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(peregrinos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Peregrinos', () => {
      const peregrinos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(peregrinos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Peregrinos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Peregrinos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Peregrinos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPeregrinosToCollectionIfMissing', () => {
      it('should add a Peregrinos to an empty array', () => {
        const peregrinos: IPeregrinos = sampleWithRequiredData;
        expectedResult = service.addPeregrinosToCollectionIfMissing([], peregrinos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(peregrinos);
      });

      it('should not add a Peregrinos to an array that contains it', () => {
        const peregrinos: IPeregrinos = sampleWithRequiredData;
        const peregrinosCollection: IPeregrinos[] = [
          {
            ...peregrinos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPeregrinosToCollectionIfMissing(peregrinosCollection, peregrinos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Peregrinos to an array that doesn't contain it", () => {
        const peregrinos: IPeregrinos = sampleWithRequiredData;
        const peregrinosCollection: IPeregrinos[] = [sampleWithPartialData];
        expectedResult = service.addPeregrinosToCollectionIfMissing(peregrinosCollection, peregrinos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(peregrinos);
      });

      it('should add only unique Peregrinos to an array', () => {
        const peregrinosArray: IPeregrinos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const peregrinosCollection: IPeregrinos[] = [sampleWithRequiredData];
        expectedResult = service.addPeregrinosToCollectionIfMissing(peregrinosCollection, ...peregrinosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const peregrinos: IPeregrinos = sampleWithRequiredData;
        const peregrinos2: IPeregrinos = sampleWithPartialData;
        expectedResult = service.addPeregrinosToCollectionIfMissing([], peregrinos, peregrinos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(peregrinos);
        expect(expectedResult).toContain(peregrinos2);
      });

      it('should accept null and undefined values', () => {
        const peregrinos: IPeregrinos = sampleWithRequiredData;
        expectedResult = service.addPeregrinosToCollectionIfMissing([], null, peregrinos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(peregrinos);
      });

      it('should return initial array if no Peregrinos is added', () => {
        const peregrinosCollection: IPeregrinos[] = [sampleWithRequiredData];
        expectedResult = service.addPeregrinosToCollectionIfMissing(peregrinosCollection, undefined, null);
        expect(expectedResult).toEqual(peregrinosCollection);
      });
    });

    describe('comparePeregrinos', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePeregrinos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22905 };
        const entity2 = null;

        const compareResult1 = service.comparePeregrinos(entity1, entity2);
        const compareResult2 = service.comparePeregrinos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22905 };
        const entity2 = { id: 6885 };

        const compareResult1 = service.comparePeregrinos(entity1, entity2);
        const compareResult2 = service.comparePeregrinos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22905 };
        const entity2 = { id: 22905 };

        const compareResult1 = service.comparePeregrinos(entity1, entity2);
        const compareResult2 = service.comparePeregrinos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
