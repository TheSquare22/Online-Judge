import dayjs from 'dayjs/esm';

import { ISubmission, NewSubmission } from './submission.model';

export const sampleWithRequiredData: ISubmission = {
  id: 80990,
};

export const sampleWithPartialData: ISubmission = {
  id: 24421,
  verdict: 'efficient Yemen frictionless',
  points: 16882,
  timeInMillis: 25133,
};

export const sampleWithFullData: ISubmission = {
  id: 55659,
  file: 'hacking Nebraska',
  verdict: 'synthesize Profit-focused program',
  details: 'visionary',
  points: 55413,
  timeInMillis: 95345,
  memoryInBytes: 1816,
  uploadDate: dayjs('2023-03-22T06:30'),
  securityKey: 'Representative',
};

export const sampleWithNewData: NewSubmission = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
