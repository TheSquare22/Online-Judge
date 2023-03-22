import dayjs from 'dayjs/esm';
import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { IUser } from 'app/entities/user/user.model';
import { ICompetitionProblem } from 'app/entities/competition-problem/competition-problem.model';

export interface ISubmission {
  id: number;
  file?: string | null;
  verdict?: string | null;
  details?: string | null;
  points?: number | null;
  timeInMillis?: number | null;
  memoryInBytes?: number | null;
  uploadDate?: dayjs.Dayjs | null;
  securityKey?: string | null;
  tags?: Pick<ITagCollection, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
  competitionProblem?: Pick<ICompetitionProblem, 'id'> | null;
}

export type NewSubmission = Omit<ISubmission, 'id'> & { id: null };
