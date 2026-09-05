import dayjs from 'dayjs/esm';

import { IPaquete, NewPaquete } from './paquete.model';

export const sampleWithRequiredData: IPaquete = {
  id: 8585,
  periodo: dayjs('2023-12-10'),
};

export const sampleWithPartialData: IPaquete = {
  id: 12749,
  nombre: 'sightseeing',
  periodo: dayjs('2023-12-10'),
  estado: 'FACTURADO',
};

export const sampleWithFullData: IPaquete = {
  id: 23823,
  nombre: 'truthfully whisper upon',
  periodo: dayjs('2023-12-10'),
  estado: 'ARCHIVADO',
  descripcion: 'log concerning neat',
  tipoIva: 'RESP_INSCRIPTO',
};

export const sampleWithNewData: NewPaquete = {
  periodo: dayjs('2023-12-10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
