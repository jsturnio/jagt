import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { INomenclador } from '../nomenclador.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../nomenclador.test-samples';

import { NomencladorService } from './nomenclador.service';

const requireRestSample: INomenclador = {
  ...sampleWithRequiredData,
};

describe('Nomenclador Service', () => {
  let service: NomencladorService;
  let httpMock: HttpTestingController;
  let expectedResult: INomenclador | INomenclador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(NomencladorService);
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

    it('should create a Nomenclador', () => {
      const nomenclador = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(nomenclador).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Nomenclador', () => {
      const nomenclador = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(nomenclador).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Nomenclador', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Nomenclador', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Nomenclador', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addNomencladorToCollectionIfMissing', () => {
      it('should add a Nomenclador to an empty array', () => {
        const nomenclador: INomenclador = sampleWithRequiredData;
        expectedResult = service.addNomencladorToCollectionIfMissing([], nomenclador);
        expect(expectedResult).toEqual([nomenclador]);
      });

      it('should not add a Nomenclador to an array that contains it', () => {
        const nomenclador: INomenclador = sampleWithRequiredData;
        const nomencladorCollection: INomenclador[] = [
          {
            ...nomenclador,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNomencladorToCollectionIfMissing(nomencladorCollection, nomenclador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Nomenclador to an array that doesn't contain it", () => {
        const nomenclador: INomenclador = sampleWithRequiredData;
        const nomencladorCollection: INomenclador[] = [sampleWithPartialData];
        expectedResult = service.addNomencladorToCollectionIfMissing(nomencladorCollection, nomenclador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nomenclador);
      });

      it('should add only unique Nomenclador to an array', () => {
        const nomencladorArray: INomenclador[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const nomencladorCollection: INomenclador[] = [sampleWithRequiredData];
        expectedResult = service.addNomencladorToCollectionIfMissing(nomencladorCollection, ...nomencladorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nomenclador: INomenclador = sampleWithRequiredData;
        const nomenclador2: INomenclador = sampleWithPartialData;
        expectedResult = service.addNomencladorToCollectionIfMissing([], nomenclador, nomenclador2);
        expect(expectedResult).toEqual([nomenclador, nomenclador2]);
      });

      it('should accept null and undefined values', () => {
        const nomenclador: INomenclador = sampleWithRequiredData;
        expectedResult = service.addNomencladorToCollectionIfMissing([], null, nomenclador, undefined);
        expect(expectedResult).toEqual([nomenclador]);
      });

      it('should return initial array if no Nomenclador is added', () => {
        const nomencladorCollection: INomenclador[] = [sampleWithRequiredData];
        expectedResult = service.addNomencladorToCollectionIfMissing(nomencladorCollection, undefined, null);
        expect(expectedResult).toEqual(nomencladorCollection);
      });
    });

    describe('compareNomenclador', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNomenclador(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21757 };
        const entity2 = null;

        const compareResult1 = service.compareNomenclador(entity1, entity2);
        const compareResult2 = service.compareNomenclador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21757 };
        const entity2 = { id: 17440 };

        const compareResult1 = service.compareNomenclador(entity1, entity2);
        const compareResult2 = service.compareNomenclador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 21757 };
        const entity2 = { id: 21757 };

        const compareResult1 = service.compareNomenclador(entity1, entity2);
        const compareResult2 = service.compareNomenclador(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
