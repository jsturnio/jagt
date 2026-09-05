import dayjs from 'dayjs/esm';

import { IBioquimico, NewBioquimico } from './bioquimico.model';

export const sampleWithRequiredData: IBioquimico = {
  id: 21543,
  tipoIva: 'RESP_NO_INSCRIPTO',
  email: 'L@=dk',
  genero: 'MUJER',
};

export const sampleWithPartialData: IBioquimico = {
  id: 26487,
  tipoIva: 'RESP_INSCRIPTO',
  estado: 'NO_CARGA',
  email: '{7,9ve@&qG|+(',
  fechaIngreso: dayjs('2023-12-10'),
  nombreEnDosep: 'parallel',
  ingBrutos: 'ah license',
  nroJubilacion: 'aboard',
  genero: 'HOMBRE',
};

export const sampleWithFullData: IBioquimico = {
  id: 25185,
  matricula: 11327,
  cuit: 'tightly with',
  tipoIva: 'RESP_INSCRIPTO',
  domicilioProfesional: 'since submitter sniff',
  estado: 'SOCIO_ACTIVO',
  email: '$=@d>',
  fechaIngreso: dayjs('2023-12-10'),
  telefono: '149887928',
  nombreEnDosep: 'glass wiggly boldly',
  nroPrestadorOsde: 20547,
  ingBrutos: 'notwithstanding wher',
  nroJubilacion: 'internationalize',
  nroLaboratorio: 'meanwhile ouch blah',
  genero: 'HOMBRE',
};

export const sampleWithNewData: NewBioquimico = {
  tipoIva: 'RESP_INSCRIPTO',
  email: '&ida@`',
  genero: 'MUJER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
