import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IPlanMutual } from '../plan-mutual.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../plan-mutual.test-samples';

import { PlanMutualService } from './plan-mutual.service';

const requireRestSample: IPlanMutual = {
  ...sampleWithRequiredData,
};

describe('PlanMutual Service', () => {
  let service: PlanMutualService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlanMutual | IPlanMutual[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlanMutualService);
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

    it('should create a PlanMutual', () => {
      const planMutual = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(planMutual).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlanMutual', () => {
      const planMutual = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(planMutual).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlanMutual', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlanMutual', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlanMutual', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPlanMutualToCollectionIfMissing', () => {
      it('should add a PlanMutual to an empty array', () => {
        const planMutual: IPlanMutual = sampleWithRequiredData;
        expectedResult = service.addPlanMutualToCollectionIfMissing([], planMutual);
        expect(expectedResult).toEqual([planMutual]);
      });

      it('should not add a PlanMutual to an array that contains it', () => {
        const planMutual: IPlanMutual = sampleWithRequiredData;
        const planMutualCollection: IPlanMutual[] = [
          {
            ...planMutual,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlanMutualToCollectionIfMissing(planMutualCollection, planMutual);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlanMutual to an array that doesn't contain it", () => {
        const planMutual: IPlanMutual = sampleWithRequiredData;
        const planMutualCollection: IPlanMutual[] = [sampleWithPartialData];
        expectedResult = service.addPlanMutualToCollectionIfMissing(planMutualCollection, planMutual);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planMutual);
      });

      it('should add only unique PlanMutual to an array', () => {
        const planMutualArray: IPlanMutual[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const planMutualCollection: IPlanMutual[] = [sampleWithRequiredData];
        expectedResult = service.addPlanMutualToCollectionIfMissing(planMutualCollection, ...planMutualArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const planMutual: IPlanMutual = sampleWithRequiredData;
        const planMutual2: IPlanMutual = sampleWithPartialData;
        expectedResult = service.addPlanMutualToCollectionIfMissing([], planMutual, planMutual2);
        expect(expectedResult).toEqual([planMutual, planMutual2]);
      });

      it('should accept null and undefined values', () => {
        const planMutual: IPlanMutual = sampleWithRequiredData;
        expectedResult = service.addPlanMutualToCollectionIfMissing([], null, planMutual, undefined);
        expect(expectedResult).toEqual([planMutual]);
      });

      it('should return initial array if no PlanMutual is added', () => {
        const planMutualCollection: IPlanMutual[] = [sampleWithRequiredData];
        expectedResult = service.addPlanMutualToCollectionIfMissing(planMutualCollection, undefined, null);
        expect(expectedResult).toEqual(planMutualCollection);
      });
    });

    describe('comparePlanMutual', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlanMutual(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5137 };
        const entity2 = null;

        const compareResult1 = service.comparePlanMutual(entity1, entity2);
        const compareResult2 = service.comparePlanMutual(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5137 };
        const entity2 = { id: 28218 };

        const compareResult1 = service.comparePlanMutual(entity1, entity2);
        const compareResult2 = service.comparePlanMutual(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5137 };
        const entity2 = { id: 5137 };

        const compareResult1 = service.comparePlanMutual(entity1, entity2);
        const compareResult2 = service.comparePlanMutual(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
