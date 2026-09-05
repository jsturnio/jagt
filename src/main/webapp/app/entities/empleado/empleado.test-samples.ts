import dayjs from 'dayjs/esm';

import { IEmpleado, NewEmpleado } from './empleado.model';

export const sampleWithRequiredData: IEmpleado = {
  id: 9977,
  tipoIva: 'RESP_NO_INSCRIPTO',
  email: 'YG%G)@3\\',
  genero: 'HOMBRE',
};

export const sampleWithPartialData: IEmpleado = {
  id: 27879,
  nombre: 'corny kookily onto',
  apellido: 'orange tag',
  tipoIva: 'RESP_NO_INSCRIPTO',
  email: 'h)Q@D1a|Mp',
  genero: 'HOMBRE',
};

export const sampleWithFullData: IEmpleado = {
  id: 30067,
  nombre: 'sweetly metallic',
  apellido: 'singing tinderbox',
  cuit: 'as waterlogged',
  tipoIva: 'RESP_NO_INSCRIPTO',
  domicilio: 'valiantly',
  email: 'U.32@tuT',
  fechaIngreso: dayjs('2023-12-10'),
  telefono: '584.130-342',
  genero: 'MUJER',
};

export const sampleWithNewData: NewEmpleado = {
  tipoIva: 'RESP_NO_INSCRIPTO',
  email: "|;I2?[@YA'*",
  genero: 'HOMBRE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
