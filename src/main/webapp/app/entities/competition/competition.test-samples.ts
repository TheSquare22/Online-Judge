import { ICompetition, NewCompetition } from './competition.model';

export const sampleWithRequiredData: ICompetition = {
  id: 91805,
};

export const sampleWithPartialData: ICompetition = {
  id: 35858,
  description: 'Supervisor CSS',
};

export const sampleWithFullData: ICompetition = {
  id: 80473,
  label: 'violet',
  description: 'Frozen Qatar',
  order: 61523,
};

export const sampleWithNewData: NewCompetition = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
