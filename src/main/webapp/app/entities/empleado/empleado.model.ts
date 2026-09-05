import dayjs from 'dayjs/esm';

import { Gender } from 'app/entities/enumerations/gender.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { IUser } from 'app/entities/user/user.model';

export interface IEmpleado {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  cuit?: string | null;
  tipoIva?: keyof typeof TipoIva | null;
  domicilio?: string | null;
  email?: string | null;
  fechaIngreso?: dayjs.Dayjs | null;
  telefono?: string | null;
  genero?: keyof typeof Gender | null;
  usuario?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEmpleado = Omit<IEmpleado, 'id'> & { id: null };
