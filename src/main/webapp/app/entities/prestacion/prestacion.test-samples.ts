import { IPrestacion, NewPrestacion } from './prestacion.model';

export const sampleWithRequiredData: IPrestacion = {
  id: 27342,
  codigo: 5824,
};

export const sampleWithPartialData: IPrestacion = {
  id: 31659,
  codigo: 18610,
  valorUb: 13873.43,
  reqAutorizacion: false,
};

export const sampleWithFullData: IPrestacion = {
  id: 15051,
  codigo: 1913,
  codigoInos: 'aha',
  valorUb: 8834.18,
  reqAutorizacion: false,
};

export const sampleWithNewData: NewPrestacion = {
  codigo: 5684,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
