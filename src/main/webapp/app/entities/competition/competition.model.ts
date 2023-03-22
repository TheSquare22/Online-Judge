export interface ICompetition {
  id: number;
  label?: string | null;
  description?: string | null;
  order?: number | null;
  parent?: Pick<ICompetition, 'id'> | null;
}

export type NewCompetition = Omit<ICompetition, 'id'> & { id: null };
