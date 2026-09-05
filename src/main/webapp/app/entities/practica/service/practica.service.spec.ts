import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPractica } from '../practica.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../practica.test-samples';

import { PracticaService } from './practica.service';

const requireRestSample: IPractica = {
  ...sampleWithRequiredData,
};

describe('Practica Service', () => {
  let service: PracticaService;
  let httpMock: HttpTestingController;
  let expectedResult: IPractica | IPractica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PracticaService);
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

    it('should create a Practica', () => {
      const practica = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(practica).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Practica', () => {
      const practica = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(practica).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Practica', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Practica', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Practica', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPracticaToCollectionIfMissing', () => {
      it('should add a Practica to an empty array', () => {
        const practica: IPractica = sampleWithRequiredData;
        expectedResult = service.addPracticaToCollectionIfMissing([], practica);
        expect(expectedResult).toEqual([practica]);
      });

      it('should not add a Practica to an array that contains it', () => {
        const practica: IPractica = sampleWithRequiredData;
        const practicaCollection: IPractica[] = [
          {
            ...practica,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPracticaToCollectionIfMissing(practicaCollection, practica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Practica to an array that doesn't contain it", () => {
        const practica: IPractica = sampleWithRequiredData;
        const practicaCollection: IPractica[] = [sampleWithPartialData];
        expectedResult = service.addPracticaToCollectionIfMissing(practicaCollection, practica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(practica);
      });

      it('should add only unique Practica to an array', () => {
        const practicaArray: IPractica[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const practicaCollection: IPractica[] = [sampleWithRequiredData];
        expectedResult = service.addPracticaToCollectionIfMissing(practicaCollection, ...practicaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const practica: IPractica = sampleWithRequiredData;
        const practica2: IPractica = sampleWithPartialData;
        expectedResult = service.addPracticaToCollectionIfMissing([], practica, practica2);
        expect(expectedResult).toEqual([practica, practica2]);
      });

      it('should accept null and undefined values', () => {
        const practica: IPractica = sampleWithRequiredData;
        expectedResult = service.addPracticaToCollectionIfMissing([], null, practica, undefined);
        expect(expectedResult).toEqual([practica]);
      });

      it('should return initial array if no Practica is added', () => {
        const practicaCollection: IPractica[] = [sampleWithRequiredData];
        expectedResult = service.addPracticaToCollectionIfMissing(practicaCollection, undefined, null);
        expect(expectedResult).toEqual(practicaCollection);
      });
    });

    describe('comparePractica', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePractica(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7449 };
        const entity2 = null;

        const compareResult1 = service.comparePractica(entity1, entity2);
        const compareResult2 = service.comparePractica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7449 };
        const entity2 = { id: 14030 };

        const compareResult1 = service.comparePractica(entity1, entity2);
        const compareResult2 = service.comparePractica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7449 };
        const entity2 = { id: 7449 };

        const compareResult1 = service.comparePractica(entity1, entity2);
        const compareResult2 = service.comparePractica(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
