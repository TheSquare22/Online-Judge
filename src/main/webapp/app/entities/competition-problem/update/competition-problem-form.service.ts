import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompetitionProblem, NewCompetitionProblem } from '../competition-problem.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompetitionProblem for edit and NewCompetitionProblemFormGroupInput for create.
 */
type CompetitionProblemFormGroupInput = ICompetitionProblem | PartialWithRequiredKeyOf<NewCompetitionProblem>;

type CompetitionProblemFormDefaults = Pick<NewCompetitionProblem, 'id'>;

type CompetitionProblemFormGroupContent = {
  id: FormControl<ICompetitionProblem['id'] | NewCompetitionProblem['id']>;
  order: FormControl<ICompetitionProblem['order']>;
  problem: FormControl<ICompetitionProblem['problem']>;
  competition: FormControl<ICompetitionProblem['competition']>;
};

export type CompetitionProblemFormGroup = FormGroup<CompetitionProblemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompetitionProblemFormService {
  createCompetitionProblemFormGroup(competitionProblem: CompetitionProblemFormGroupInput = { id: null }): CompetitionProblemFormGroup {
    const competitionProblemRawValue = {
      ...this.getFormDefaults(),
      ...competitionProblem,
    };
    return new FormGroup<CompetitionProblemFormGroupContent>({
      id: new FormControl(
        { value: competitionProblemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      order: new FormControl(competitionProblemRawValue.order),
      problem: new FormControl(competitionProblemRawValue.problem),
      competition: new FormControl(competitionProblemRawValue.competition),
    });
  }

  getCompetitionProblem(form: CompetitionProblemFormGroup): ICompetitionProblem | NewCompetitionProblem {
    return form.getRawValue() as ICompetitionProblem | NewCompetitionProblem;
  }

  resetForm(form: CompetitionProblemFormGroup, competitionProblem: CompetitionProblemFormGroupInput): void {
    const competitionProblemRawValue = { ...this.getFormDefaults(), ...competitionProblem };
    form.reset(
      {
        ...competitionProblemRawValue,
        id: { value: competitionProblemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompetitionProblemFormDefaults {
    return {
      id: null,
    };
  }
}
