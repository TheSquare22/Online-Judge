import { ITagCollectionTag, NewTagCollectionTag } from './tag-collection-tag.model';

export const sampleWithRequiredData: ITagCollectionTag = {
  id: 19621,
};

export const sampleWithPartialData: ITagCollectionTag = {
  id: 89915,
};

export const sampleWithFullData: ITagCollectionTag = {
  id: 18447,
};

export const sampleWithNewData: NewTagCollectionTag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
