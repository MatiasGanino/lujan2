import { IPeregrinos, NewPeregrinos } from './peregrinos.model';

export const sampleWithRequiredData: IPeregrinos = {
  id: 22109,
  apellido: 'before seagull',
  nombre: 'deeply roasted sauerkraut',
  mayorMenor: 'MAYOR',
  salida: 'LINIERS',
  formaPago: 'TRANSFERENCIA',
};

export const sampleWithPartialData: IPeregrinos = {
  id: 20985,
  apellido: 'on first quicker',
  nombre: 'meal midst yesterday',
  mayorMenor: 'MENOR',
  salida: 'LINIERS',
  pago: 6605,
  formaPago: 'OTRO',
};

export const sampleWithFullData: IPeregrinos = {
  id: 23754,
  numeroEspecial: 31941,
  apellido: 'ugh aside hmph',
  nombre: 'garrote search phew',
  numeroDocumento: 'embossing',
  telefono: 'bravely vet westernise',
  mayorMenor: 'MENOR',
  salida: 'LINIERS',
  pago: 11217,
  formaPago: 'TRANSFERENCIA',
  aclaraciones: 'whoa',
  completoFormulario: false,
};

export const sampleWithNewData: NewPeregrinos = {
  apellido: 'submitter yowza',
  nombre: 'ugh sadly',
  mayorMenor: 'MENOR',
  salida: 'LA_REJA',
  formaPago: 'TRANSFERENCIA',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
