import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { ITag } from 'app/entities/tag/tag.model';

export interface ITagCollectionTag {
  id: number;
  collection?: Pick<ITagCollection, 'id'> | null;
  tag?: Pick<ITag, 'id'> | null;
}

export type NewTagCollectionTag = Omit<ITagCollectionTag, 'id'> & { id: null };
