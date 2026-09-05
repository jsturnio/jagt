import { INomenclador } from 'app/entities/nomenclador/nomenclador.model';

export interface IPrestacion {
  id: number;
  codigo?: number | null;
  codigoInos?: string | null;
  valorUb?: number | null;
  reqAutorizacion?: boolean | null;
  nomenclador?: Pick<INomenclador, 'id' | 'nombre'> | null;
}

export type NewPrestacion = Omit<IPrestacion, 'id'> & { id: null };
