import dayjs from 'dayjs/esm';

import { EstadoSocio } from 'app/entities/enumerations/estado-socio.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { IUser } from 'app/entities/user/user.model';

export interface IBioquimico {
  id: number;
  matricula?: number | null;
  cuit?: string | null;
  tipoIva?: keyof typeof TipoIva | null;
  domicilioProfesional?: string | null;
  estado?: keyof typeof EstadoSocio | null;
  email?: string | null;
  fechaIngreso?: dayjs.Dayjs | null;
  telefono?: string | null;
  nombreEnDosep?: string | null;
  nroPrestadorOsde?: number | null;
  ingBrutos?: string | null;
  nroJubilacion?: string | null;
  nroLaboratorio?: string | null;
  nombreCompleto?: string | null;
  genero?: keyof typeof Gender | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewBioquimico = Omit<IBioquimico, 'id'> & { id: null };
