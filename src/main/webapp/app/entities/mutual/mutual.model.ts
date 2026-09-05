import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';

export interface IMutual {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  cuit?: string | null;
  tipoIva?: keyof typeof TipoIva | null;
  domicilio?: string | null;
  telefono?: string | null;
  email?: string | null;
  habilitada?: boolean | null;
}

export type NewMutual = Omit<IMutual, 'id'> & { id: null };
