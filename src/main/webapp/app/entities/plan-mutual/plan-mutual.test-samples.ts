import { IPlanMutual, NewPlanMutual } from './plan-mutual.model';

export const sampleWithRequiredData: IPlanMutual = {
  id: 20104,
  categoria: 'gah gro',
  etiquetaReporte: 'silt an',
};

export const sampleWithPartialData: IPlanMutual = {
  id: 30190,
  categoria: 'confide',
  etiquetaReporte: 'yum dis',
  prestacionesImportadas: false,
};

export const sampleWithFullData: IPlanMutual = {
  id: 3140,
  categoria: 'slipper',
  etiquetaReporte: 'ick lit',
  prestacionesImportadas: false,
};

export const sampleWithNewData: NewPlanMutual = {
  categoria: 'knowled',
  etiquetaReporte: 'volunta',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
