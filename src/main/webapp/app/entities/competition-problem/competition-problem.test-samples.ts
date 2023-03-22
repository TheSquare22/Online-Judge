import { ICompetitionProblem, NewCompetitionProblem } from './competition-problem.model';

export const sampleWithRequiredData: ICompetitionProblem = {
  id: 95760,
};

export const sampleWithPartialData: ICompetitionProblem = {
  id: 50408,
  order: 10082,
};

export const sampleWithFullData: ICompetitionProblem = {
  id: 90685,
  order: 4912,
};

export const sampleWithNewData: NewCompetitionProblem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
