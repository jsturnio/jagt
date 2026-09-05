import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IMutual } from '../mutual.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../mutual.test-samples';

import { MutualService } from './mutual.service';

const requireRestSample: IMutual = {
  ...sampleWithRequiredData,
};

describe('Mutual Service', () => {
  let service: MutualService;
  let httpMock: HttpTestingController;
  let expectedResult: IMutual | IMutual[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MutualService);
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

    it('should create a Mutual', () => {
      const mutual = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mutual).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mutual', () => {
      const mutual = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mutual).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mutual', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mutual', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mutual', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addMutualToCollectionIfMissing', () => {
      it('should add a Mutual to an empty array', () => {
        const mutual: IMutual = sampleWithRequiredData;
        expectedResult = service.addMutualToCollectionIfMissing([], mutual);
        expect(expectedResult).toEqual([mutual]);
      });

      it('should not add a Mutual to an array that contains it', () => {
        const mutual: IMutual = sampleWithRequiredData;
        const mutualCollection: IMutual[] = [
          {
            ...mutual,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, mutual);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mutual to an array that doesn't contain it", () => {
        const mutual: IMutual = sampleWithRequiredData;
        const mutualCollection: IMutual[] = [sampleWithPartialData];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, mutual);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mutual);
      });

      it('should add only unique Mutual to an array', () => {
        const mutualArray: IMutual[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mutualCollection: IMutual[] = [sampleWithRequiredData];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, ...mutualArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mutual: IMutual = sampleWithRequiredData;
        const mutual2: IMutual = sampleWithPartialData;
        expectedResult = service.addMutualToCollectionIfMissing([], mutual, mutual2);
        expect(expectedResult).toEqual([mutual, mutual2]);
      });

      it('should accept null and undefined values', () => {
        const mutual: IMutual = sampleWithRequiredData;
        expectedResult = service.addMutualToCollectionIfMissing([], null, mutual, undefined);
        expect(expectedResult).toEqual([mutual]);
      });

      it('should return initial array if no Mutual is added', () => {
        const mutualCollection: IMutual[] = [sampleWithRequiredData];
        expectedResult = service.addMutualToCollectionIfMissing(mutualCollection, undefined, null);
        expect(expectedResult).toEqual(mutualCollection);
      });
    });

    describe('compareMutual', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMutual(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4576 };
        const entity2 = null;

        const compareResult1 = service.compareMutual(entity1, entity2);
        const compareResult2 = service.compareMutual(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4576 };
        const entity2 = { id: 14602 };

        const compareResult1 = service.compareMutual(entity1, entity2);
        const compareResult2 = service.compareMutual(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4576 };
        const entity2 = { id: 4576 };

        const compareResult1 = service.compareMutual(entity1, entity2);
        const compareResult2 = service.compareMutual(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
