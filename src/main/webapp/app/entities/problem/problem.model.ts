import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';

export interface IProblem {
  id: number;
  title?: string | null;
  directory?: string | null;
  version?: number | null;
  tags?: Pick<ITagCollection, 'id'> | null;
}

export type NewProblem = Omit<IProblem, 'id'> & { id: null };
