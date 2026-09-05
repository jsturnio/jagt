import dayjs from 'dayjs/esm';

import { EstadoPaquete } from 'app/entities/enumerations/estado-paquete.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { IPlanMutual } from 'app/entities/plan-mutual/plan-mutual.model';

export interface IPaquete {
  id: number;
  paqDescrip?: string | null;
  nombre?: string | null;
  periodo?: dayjs.Dayjs | null;
  estado?: keyof typeof EstadoPaquete | null;
  descripcion?: string | null;
  tipoIva?: keyof typeof TipoIva | null;
  plan?: Pick<IPlanMutual, 'id' | 'categoria'> | null;
}

export type NewPaquete = Omit<IPaquete, 'id'> & { id: null };
