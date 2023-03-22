import { IProblem, NewProblem } from './problem.model';

export const sampleWithRequiredData: IProblem = {
  id: 78437,
};

export const sampleWithPartialData: IProblem = {
  id: 23821,
  title: 'Wooden withdrawal Faroe',
  version: 25457,
};

export const sampleWithFullData: IProblem = {
  id: 33730,
  title: 'Gorgeous repurpose',
  directory: 'pixel Sports ADP',
  version: 99358,
};

export const sampleWithNewData: NewProblem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
