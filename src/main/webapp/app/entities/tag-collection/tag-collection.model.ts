export interface ITagCollection {
  id: number;
}

export type NewTagCollection = Omit<ITagCollection, 'id'> & { id: null };
