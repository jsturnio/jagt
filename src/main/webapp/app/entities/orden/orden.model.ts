import dayjs from 'dayjs/esm';

import { IBioquimico } from 'app/entities/bioquimico/bioquimico.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { IPaquete } from 'app/entities/paquete/paquete.model';

export interface IOrden {
  id: number;
  afiliado?: number | null;
  protocolo?: number | null;
  fechaOrden?: dayjs.Dayjs | null;
  fechaPrescripcion?: dayjs.Dayjs | null;
  totalOrden?: number | null;
  sumaUb?: number | null;
  fechaCreacion?: dayjs.Dayjs | null;
  paquete?: Pick<IPaquete, 'id' | 'paqDescrip'> | null;
  usuario?: Pick<IEmpleado, 'id'> | null;
  bioquimico?: Pick<IBioquimico, 'id' | 'nombreCompleto'> | null;
}

export type NewOrden = Omit<IOrden, 'id'> & { id: null };
