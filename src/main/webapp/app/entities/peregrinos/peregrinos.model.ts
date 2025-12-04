import { TipoPersona } from 'app/entities/enumerations/tipo-persona.model';
import { TipoSalida } from 'app/entities/enumerations/tipo-salida.model';
import { TipoFormaPago } from 'app/entities/enumerations/tipo-forma-pago.model';

export interface IPeregrinos {
  id: number;
  numeroEspecial?: number | null;
  apellido?: string | null;
  nombre?: string | null;
  numeroDocumento?: string | null;
  telefono?: string | null;
  mayorMenor?: keyof typeof TipoPersona | null;
  salida?: keyof typeof TipoSalida | null;
  pago?: number | null;
  formaPago?: keyof typeof TipoFormaPago | null;
  aclaraciones?: string | null;
  completoFormulario?: boolean | null;
}

export type NewPeregrinos = Omit<IPeregrinos, 'id'> & { id: null };
