import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPrestacion } from '../prestacion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../prestacion.test-samples';

import { PrestacionService } from './prestacion.service';

const requireRestSample: IPrestacion = {
  ...sampleWithRequiredData,
};

describe('Prestacion Service', () => {
  let service: PrestacionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrestacion | IPrestacion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PrestacionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Prestacion', () => {
      const prestacion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prestacion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Prestacion', () => {
      const prestacion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prestacion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Prestacion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Prestacion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Prestacion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPrestacionToCollectionIfMissing', () => {
      it('should add a Prestacion to an empty array', () => {
        const prestacion: IPrestacion = sampleWithRequiredData;
        expectedResult = service.addPrestacionToCollectionIfMissing([], prestacion);
        expect(expectedResult).toEqual([prestacion]);
      });

      it('should not add a Prestacion to an array that contains it', () => {
        const prestacion: IPrestacion = sampleWithRequiredData;
        const prestacionCollection: IPrestacion[] = [
          {
            ...prestacion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrestacionToCollectionIfMissing(prestacionCollection, prestacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Prestacion to an array that doesn't contain it", () => {
        const prestacion: IPrestacion = sampleWithRequiredData;
        const prestacionCollection: IPrestacion[] = [sampleWithPartialData];
        expectedResult = service.addPrestacionToCollectionIfMissing(prestacionCollection, prestacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prestacion);
      });

      it('should add only unique Prestacion to an array', () => {
        const prestacionArray: IPrestacion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prestacionCollection: IPrestacion[] = [sampleWithRequiredData];
        expectedResult = service.addPrestacionToCollectionIfMissing(prestacionCollection, ...prestacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prestacion: IPrestacion = sampleWithRequiredData;
        const prestacion2: IPrestacion = sampleWithPartialData;
        expectedResult = service.addPrestacionToCollectionIfMissing([], prestacion, prestacion2);
        expect(expectedResult).toEqual([prestacion, prestacion2]);
      });

      it('should accept null and undefined values', () => {
        const prestacion: IPrestacion = sampleWithRequiredData;
        expectedResult = service.addPrestacionToCollectionIfMissing([], null, prestacion, undefined);
        expect(expectedResult).toEqual([prestacion]);
      });

      it('should return initial array if no Prestacion is added', () => {
        const prestacionCollection: IPrestacion[] = [sampleWithRequiredData];
        expectedResult = service.addPrestacionToCollectionIfMissing(prestacionCollection, undefined, null);
        expect(expectedResult).toEqual(prestacionCollection);
      });
    });

    describe('comparePrestacion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrestacion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17259 };
        const entity2 = null;

        const compareResult1 = service.comparePrestacion(entity1, entity2);
        const compareResult2 = service.comparePrestacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17259 };
        const entity2 = { id: 23229 };

        const compareResult1 = service.comparePrestacion(entity1, entity2);
        const compareResult2 = service.comparePrestacion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 17259 };
        const entity2 = { id: 17259 };

        const compareResult1 = service.comparePrestacion(entity1, entity2);
        const compareResult2 = service.comparePrestacion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
