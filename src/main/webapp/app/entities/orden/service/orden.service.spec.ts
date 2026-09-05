import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IOrden } from '../orden.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../orden.test-samples';

import { OrdenService, RestOrden } from './orden.service';

const requireRestSample: RestOrden = {
  ...sampleWithRequiredData,
  fechaOrden: sampleWithRequiredData.fechaOrden?.format(DATE_FORMAT),
  fechaPrescripcion: sampleWithRequiredData.fechaPrescripcion?.format(DATE_FORMAT),
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
};

describe('Orden Service', () => {
  let service: OrdenService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrden | IOrden[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OrdenService);
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

    it('should create a Orden', () => {
      const orden = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orden).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Orden', () => {
      const orden = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orden).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Orden', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Orden', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Orden', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addOrdenToCollectionIfMissing', () => {
      it('should add a Orden to an empty array', () => {
        const orden: IOrden = sampleWithRequiredData;
        expectedResult = service.addOrdenToCollectionIfMissing([], orden);
        expect(expectedResult).toEqual([orden]);
      });

      it('should not add a Orden to an array that contains it', () => {
        const orden: IOrden = sampleWithRequiredData;
        const ordenCollection: IOrden[] = [
          {
            ...orden,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, orden);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Orden to an array that doesn't contain it", () => {
        const orden: IOrden = sampleWithRequiredData;
        const ordenCollection: IOrden[] = [sampleWithPartialData];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, orden);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orden);
      });

      it('should add only unique Orden to an array', () => {
        const ordenArray: IOrden[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ordenCollection: IOrden[] = [sampleWithRequiredData];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, ...ordenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orden: IOrden = sampleWithRequiredData;
        const orden2: IOrden = sampleWithPartialData;
        expectedResult = service.addOrdenToCollectionIfMissing([], orden, orden2);
        expect(expectedResult).toEqual([orden, orden2]);
      });

      it('should accept null and undefined values', () => {
        const orden: IOrden = sampleWithRequiredData;
        expectedResult = service.addOrdenToCollectionIfMissing([], null, orden, undefined);
        expect(expectedResult).toEqual([orden]);
      });

      it('should return initial array if no Orden is added', () => {
        const ordenCollection: IOrden[] = [sampleWithRequiredData];
        expectedResult = service.addOrdenToCollectionIfMissing(ordenCollection, undefined, null);
        expect(expectedResult).toEqual(ordenCollection);
      });
    });

    describe('compareOrden', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrden(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10408 };
        const entity2 = null;

        const compareResult1 = service.compareOrden(entity1, entity2);
        const compareResult2 = service.compareOrden(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10408 };
        const entity2 = { id: 29983 };

        const compareResult1 = service.compareOrden(entity1, entity2);
        const compareResult2 = service.compareOrden(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 10408 };
        const entity2 = { id: 10408 };

        const compareResult1 = service.compareOrden(entity1, entity2);
        const compareResult2 = service.compareOrden(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
