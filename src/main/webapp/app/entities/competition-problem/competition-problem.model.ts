import { IProblem } from 'app/entities/problem/problem.model';
import { ICompetition } from 'app/entities/competition/competition.model';

export interface ICompetitionProblem {
  id: number;
  order?: number | null;
  problem?: Pick<IProblem, 'id'> | null;
  competition?: Pick<ICompetition, 'id'> | null;
}

export type NewCompetitionProblem = Omit<ICompetitionProblem, 'id'> & { id: null };
