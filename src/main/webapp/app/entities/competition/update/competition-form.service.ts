import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompetition, NewCompetition } from '../competition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompetition for edit and NewCompetitionFormGroupInput for create.
 */
type CompetitionFormGroupInput = ICompetition | PartialWithRequiredKeyOf<NewCompetition>;

type CompetitionFormDefaults = Pick<NewCompetition, 'id'>;

type CompetitionFormGroupContent = {
  id: FormControl<ICompetition['id'] | NewCompetition['id']>;
  label: FormControl<ICompetition['label']>;
  description: FormControl<ICompetition['description']>;
  order: FormControl<ICompetition['order']>;
  parent: FormControl<ICompetition['parent']>;
};

export type CompetitionFormGroup = FormGroup<CompetitionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompetitionFormService {
  createCompetitionFormGroup(competition: CompetitionFormGroupInput = { id: null }): CompetitionFormGroup {
    const competitionRawValue = {
      ...this.getFormDefaults(),
      ...competition,
    };
    return new FormGroup<CompetitionFormGroupContent>({
      id: new FormControl(
        { value: competitionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      label: new FormControl(competitionRawValue.label),
      description: new FormControl(competitionRawValue.description),
      order: new FormControl(competitionRawValue.order),
      parent: new FormControl(competitionRawValue.parent),
    });
  }

  getCompetition(form: CompetitionFormGroup): ICompetition | NewCompetition {
    return form.getRawValue() as ICompetition | NewCompetition;
  }

  resetForm(form: CompetitionFormGroup, competition: CompetitionFormGroupInput): void {
    const competitionRawValue = { ...this.getFormDefaults(), ...competition };
    form.reset(
      {
        ...competitionRawValue,
        id: { value: competitionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompetitionFormDefaults {
    return {
      id: null,
    };
  }
}
