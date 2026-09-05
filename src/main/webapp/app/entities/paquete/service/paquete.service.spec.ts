import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IPaquete } from '../paquete.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../paquete.test-samples';

import { PaqueteService, RestPaquete } from './paquete.service';

const requireRestSample: RestPaquete = {
  ...sampleWithRequiredData,
  periodo: sampleWithRequiredData.periodo?.format(DATE_FORMAT),
};

describe('Paquete Service', () => {
  let service: PaqueteService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaquete | IPaquete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaqueteService);
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

    it('should create a Paquete', () => {
      const paquete = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paquete).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Paquete', () => {
      const paquete = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paquete).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Paquete', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Paquete', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Paquete', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPaqueteToCollectionIfMissing', () => {
      it('should add a Paquete to an empty array', () => {
        const paquete: IPaquete = sampleWithRequiredData;
        expectedResult = service.addPaqueteToCollectionIfMissing([], paquete);
        expect(expectedResult).toEqual([paquete]);
      });

      it('should not add a Paquete to an array that contains it', () => {
        const paquete: IPaquete = sampleWithRequiredData;
        const paqueteCollection: IPaquete[] = [
          {
            ...paquete,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, paquete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Paquete to an array that doesn't contain it", () => {
        const paquete: IPaquete = sampleWithRequiredData;
        const paqueteCollection: IPaquete[] = [sampleWithPartialData];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, paquete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paquete);
      });

      it('should add only unique Paquete to an array', () => {
        const paqueteArray: IPaquete[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paqueteCollection: IPaquete[] = [sampleWithRequiredData];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, ...paqueteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paquete: IPaquete = sampleWithRequiredData;
        const paquete2: IPaquete = sampleWithPartialData;
        expectedResult = service.addPaqueteToCollectionIfMissing([], paquete, paquete2);
        expect(expectedResult).toEqual([paquete, paquete2]);
      });

      it('should accept null and undefined values', () => {
        const paquete: IPaquete = sampleWithRequiredData;
        expectedResult = service.addPaqueteToCollectionIfMissing([], null, paquete, undefined);
        expect(expectedResult).toEqual([paquete]);
      });

      it('should return initial array if no Paquete is added', () => {
        const paqueteCollection: IPaquete[] = [sampleWithRequiredData];
        expectedResult = service.addPaqueteToCollectionIfMissing(paqueteCollection, undefined, null);
        expect(expectedResult).toEqual(paqueteCollection);
      });
    });

    describe('comparePaquete', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaquete(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 23723 };
        const entity2 = null;

        const compareResult1 = service.comparePaquete(entity1, entity2);
        const compareResult2 = service.comparePaquete(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 23723 };
        const entity2 = { id: 5295 };

        const compareResult1 = service.comparePaquete(entity1, entity2);
        const compareResult2 = service.comparePaquete(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 23723 };
        const entity2 = { id: 23723 };

        const compareResult1 = service.comparePaquete(entity1, entity2);
        const compareResult2 = service.comparePaquete(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
