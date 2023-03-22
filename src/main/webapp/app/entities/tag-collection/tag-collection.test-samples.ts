import { ITagCollection, NewTagCollection } from './tag-collection.model';

export const sampleWithRequiredData: ITagCollection = {
  id: 19429,
};

export const sampleWithPartialData: ITagCollection = {
  id: 40511,
};

export const sampleWithFullData: ITagCollection = {
  id: 15115,
};

export const sampleWithNewData: NewTagCollection = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
