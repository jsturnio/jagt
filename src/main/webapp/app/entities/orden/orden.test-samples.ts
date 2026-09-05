import dayjs from 'dayjs/esm';

import { IOrden, NewOrden } from './orden.model';

export const sampleWithRequiredData: IOrden = {
  id: 7236,
};

export const sampleWithPartialData: IOrden = {
  id: 29304,
  afiliado: 782,
  fechaPrescripcion: dayjs('2023-12-10'),
};

export const sampleWithFullData: IOrden = {
  id: 4099,
  afiliado: 21869,
  protocolo: 18264,
  fechaOrden: dayjs('2023-12-10'),
  fechaPrescripcion: dayjs('2023-12-10'),
  totalOrden: 26337.25,
  sumaUb: 15120.39,
  fechaCreacion: dayjs('2023-12-10T22:47'),
};

export const sampleWithNewData: NewOrden = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
