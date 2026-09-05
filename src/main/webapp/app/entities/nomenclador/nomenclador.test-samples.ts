import { INomenclador, NewNomenclador } from './nomenclador.model';

export const sampleWithRequiredData: INomenclador = {
  id: 5347,
  nombre: 'useless gee',
  tipo: 'INOS',
};

export const sampleWithPartialData: INomenclador = {
  id: 3590,
  nombre: 'generously b',
  tipo: 'NBU',
};

export const sampleWithFullData: INomenclador = {
  id: 20893,
  nombre: 'for rich con',
  descripcion: 'willfully',
  tipo: 'NBU',
};

export const sampleWithNewData: NewNomenclador = {
  nombre: 'inasmuch wha',
  tipo: 'NBU',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
