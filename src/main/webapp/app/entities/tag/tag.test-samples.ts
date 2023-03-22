import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 42372,
};

export const sampleWithPartialData: ITag = {
  id: 77324,
};

export const sampleWithFullData: ITag = {
  id: 72152,
  title: 'Officer Sleek Extended',
  keywords: 'Toys THX Burgs',
  visible: true,
};

export const sampleWithNewData: NewTag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
