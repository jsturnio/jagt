import { TipoNomenclador } from 'app/entities/enumerations/tipo-nomenclador.model';
import { IMutual } from 'app/entities/mutual/mutual.model';

export interface INomenclador {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  tipo?: keyof typeof TipoNomenclador | null;
  mutual?: Pick<IMutual, 'id' | 'nombre'> | null;
}

export type NewNomenclador = Omit<INomenclador, 'id'> & { id: null };
