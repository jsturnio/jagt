import { IMutual, NewMutual } from './mutual.model';

export const sampleWithRequiredData: IMutual = {
  id: 6202,
  nombre: 'bend ne',
  tipoIva: 'RESP_NO_INSCRIPTO',
};

export const sampleWithPartialData: IMutual = {
  id: 20256,
  nombre: 'ordinar',
  tipoIva: 'RESP_NO_INSCRIPTO',
  domicilio: 'toothbrush alongside',
};

export const sampleWithFullData: IMutual = {
  id: 27411,
  nombre: 'making ',
  descripcion: 'boohoo',
  cuit: 'except passionate mo',
  tipoIva: 'RESP_INSCRIPTO',
  domicilio: 'weighty hence blindly',
  telefono: '103833\\dddd',
  email: '*Bp@\\SSSSS',
  habilitada: false,
};

export const sampleWithNewData: NewMutual = {
  nombre: 'whether',
  tipoIva: 'RESP_NO_INSCRIPTO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
