import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubmission, NewSubmission } from '../submission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubmission for edit and NewSubmissionFormGroupInput for create.
 */
type SubmissionFormGroupInput = ISubmission | PartialWithRequiredKeyOf<NewSubmission>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISubmission | NewSubmission> = Omit<T, 'uploadDate'> & {
  uploadDate?: string | null;
};

type SubmissionFormRawValue = FormValueOf<ISubmission>;

type NewSubmissionFormRawValue = FormValueOf<NewSubmission>;

type SubmissionFormDefaults = Pick<NewSubmission, 'id' | 'uploadDate'>;

type SubmissionFormGroupContent = {
  id: FormControl<SubmissionFormRawValue['id'] | NewSubmission['id']>;
  file: FormControl<SubmissionFormRawValue['file']>;
  verdict: FormControl<SubmissionFormRawValue['verdict']>;
  details: FormControl<SubmissionFormRawValue['details']>;
  points: FormControl<SubmissionFormRawValue['points']>;
  timeInMillis: FormControl<SubmissionFormRawValue['timeInMillis']>;
  memoryInBytes: FormControl<SubmissionFormRawValue['memoryInBytes']>;
  uploadDate: FormControl<SubmissionFormRawValue['uploadDate']>;
  securityKey: FormControl<SubmissionFormRawValue['securityKey']>;
  tags: FormControl<SubmissionFormRawValue['tags']>;
  user: FormControl<SubmissionFormRawValue['user']>;
  competitionProblem: FormControl<SubmissionFormRawValue['competitionProblem']>;
};

export type SubmissionFormGroup = FormGroup<SubmissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubmissionFormService {
  createSubmissionFormGroup(submission: SubmissionFormGroupInput = { id: null }): SubmissionFormGroup {
    const submissionRawValue = this.convertSubmissionToSubmissionRawValue({
      ...this.getFormDefaults(),
      ...submission,
    });
    return new FormGroup<SubmissionFormGroupContent>({
      id: new FormControl(
        { value: submissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      file: new FormControl(submissionRawValue.file),
      verdict: new FormControl(submissionRawValue.verdict),
      details: new FormControl(submissionRawValue.details),
      points: new FormControl(submissionRawValue.points),
      timeInMillis: new FormControl(submissionRawValue.timeInMillis),
      memoryInBytes: new FormControl(submissionRawValue.memoryInBytes),
      uploadDate: new FormControl(submissionRawValue.uploadDate),
      securityKey: new FormControl(submissionRawValue.securityKey),
      tags: new FormControl(submissionRawValue.tags),
      user: new FormControl(submissionRawValue.user),
      competitionProblem: new FormControl(submissionRawValue.competitionProblem),
    });
  }

  getSubmission(form: SubmissionFormGroup): ISubmission | NewSubmission {
    return this.convertSubmissionRawValueToSubmission(form.getRawValue() as SubmissionFormRawValue | NewSubmissionFormRawValue);
  }

  resetForm(form: SubmissionFormGroup, submission: SubmissionFormGroupInput): void {
    const submissionRawValue = this.convertSubmissionToSubmissionRawValue({ ...this.getFormDefaults(), ...submission });
    form.reset(
      {
        ...submissionRawValue,
        id: { value: submissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SubmissionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadDate: currentTime,
    };
  }

  private convertSubmissionRawValueToSubmission(
    rawSubmission: SubmissionFormRawValue | NewSubmissionFormRawValue
  ): ISubmission | NewSubmission {
    return {
      ...rawSubmission,
      uploadDate: dayjs(rawSubmission.uploadDate, DATE_TIME_FORMAT),
    };
  }

  private convertSubmissionToSubmissionRawValue(
    submission: ISubmission | (Partial<NewSubmission> & SubmissionFormDefaults)
  ): SubmissionFormRawValue | PartialWithRequiredKeyOf<NewSubmissionFormRawValue> {
    return {
      ...submission,
      uploadDate: submission.uploadDate ? submission.uploadDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
