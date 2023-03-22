export interface ITag {
  id: number;
  title?: string | null;
  keywords?: string | null;
  visible?: boolean | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
