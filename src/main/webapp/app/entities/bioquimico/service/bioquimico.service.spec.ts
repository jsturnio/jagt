import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IBioquimico } from '../bioquimico.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bioquimico.test-samples';

import { BioquimicoService, RestBioquimico } from './bioquimico.service';

const requireRestSample: RestBioquimico = {
  ...sampleWithRequiredData,
  fechaIngreso: sampleWithRequiredData.fechaIngreso?.format(DATE_FORMAT),
};

describe('Bioquimico Service', () => {
  let service: BioquimicoService;
  let httpMock: HttpTestingController;
  let expectedResult: IBioquimico | IBioquimico[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BioquimicoService);
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

    it('should create a Bioquimico', () => {
      const bioquimico = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bioquimico).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bioquimico', () => {
      const bioquimico = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bioquimico).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bioquimico', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bioquimico', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bioquimico', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addBioquimicoToCollectionIfMissing', () => {
      it('should add a Bioquimico to an empty array', () => {
        const bioquimico: IBioquimico = sampleWithRequiredData;
        expectedResult = service.addBioquimicoToCollectionIfMissing([], bioquimico);
        expect(expectedResult).toEqual([bioquimico]);
      });

      it('should not add a Bioquimico to an array that contains it', () => {
        const bioquimico: IBioquimico = sampleWithRequiredData;
        const bioquimicoCollection: IBioquimico[] = [
          {
            ...bioquimico,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBioquimicoToCollectionIfMissing(bioquimicoCollection, bioquimico);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bioquimico to an array that doesn't contain it", () => {
        const bioquimico: IBioquimico = sampleWithRequiredData;
        const bioquimicoCollection: IBioquimico[] = [sampleWithPartialData];
        expectedResult = service.addBioquimicoToCollectionIfMissing(bioquimicoCollection, bioquimico);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bioquimico);
      });

      it('should add only unique Bioquimico to an array', () => {
        const bioquimicoArray: IBioquimico[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bioquimicoCollection: IBioquimico[] = [sampleWithRequiredData];
        expectedResult = service.addBioquimicoToCollectionIfMissing(bioquimicoCollection, ...bioquimicoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bioquimico: IBioquimico = sampleWithRequiredData;
        const bioquimico2: IBioquimico = sampleWithPartialData;
        expectedResult = service.addBioquimicoToCollectionIfMissing([], bioquimico, bioquimico2);
        expect(expectedResult).toEqual([bioquimico, bioquimico2]);
      });

      it('should accept null and undefined values', () => {
        const bioquimico: IBioquimico = sampleWithRequiredData;
        expectedResult = service.addBioquimicoToCollectionIfMissing([], null, bioquimico, undefined);
        expect(expectedResult).toEqual([bioquimico]);
      });

      it('should return initial array if no Bioquimico is added', () => {
        const bioquimicoCollection: IBioquimico[] = [sampleWithRequiredData];
        expectedResult = service.addBioquimicoToCollectionIfMissing(bioquimicoCollection, undefined, null);
        expect(expectedResult).toEqual(bioquimicoCollection);
      });
    });

    describe('compareBioquimico', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBioquimico(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24259 };
        const entity2 = null;

        const compareResult1 = service.compareBioquimico(entity1, entity2);
        const compareResult2 = service.compareBioquimico(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24259 };
        const entity2 = { id: 1371 };

        const compareResult1 = service.compareBioquimico(entity1, entity2);
        const compareResult2 = service.compareBioquimico(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 24259 };
        const entity2 = { id: 24259 };

        const compareResult1 = service.compareBioquimico(entity1, entity2);
        const compareResult2 = service.compareBioquimico(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
