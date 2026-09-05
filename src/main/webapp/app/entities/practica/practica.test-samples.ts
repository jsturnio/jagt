import { IPractica, NewPractica } from './practica.model';

export const sampleWithRequiredData: IPractica = {
  id: 11522,
  cantidad: 7370,
};

export const sampleWithPartialData: IPractica = {
  id: 17672,
  cantidad: 14966,
};

export const sampleWithFullData: IPractica = {
  id: 14731,
  cantidad: 23538,
  pvalor: 13401.47,
};

export const sampleWithNewData: NewPractica = {
  cantidad: 20067,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
