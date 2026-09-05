import { IOrden } from 'app/entities/orden/orden.model';
import { IPrestacion } from 'app/entities/prestacion/prestacion.model';

export interface IPractica {
  id: number;
  cantidad?: number | null;
  pvalor?: number | null;
  prestacion?: Pick<IPrestacion, 'id' | 'codigo'> | null;
  orden?: Pick<IOrden, 'id'> | null;
}

export type NewPractica = Omit<IPractica, 'id'> & { id: null };
