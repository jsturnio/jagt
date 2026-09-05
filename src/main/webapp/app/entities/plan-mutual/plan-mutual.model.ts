import { IMutual } from 'app/entities/mutual/mutual.model';

export interface IPlanMutual {
  id: number;
  categoria?: string | null;
  etiquetaReporte?: string | null;
  prestacionesImportadas?: boolean | null;
  mutual?: Pick<IMutual, 'id' | 'nombre'> | null;
}

export type NewPlanMutual = Omit<IPlanMutual, 'id'> & { id: null };
