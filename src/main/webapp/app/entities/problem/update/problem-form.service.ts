import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProblem, NewProblem } from '../problem.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProblem for edit and NewProblemFormGroupInput for create.
 */
type ProblemFormGroupInput = IProblem | PartialWithRequiredKeyOf<NewProblem>;

type ProblemFormDefaults = Pick<NewProblem, 'id'>;

type ProblemFormGroupContent = {
  id: FormControl<IProblem['id'] | NewProblem['id']>;
  title: FormControl<IProblem['title']>;
  directory: FormControl<IProblem['directory']>;
  version: FormControl<IProblem['version']>;
  tags: FormControl<IProblem['tags']>;
};

export type ProblemFormGroup = FormGroup<ProblemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProblemFormService {
  createProblemFormGroup(problem: ProblemFormGroupInput = { id: null }): ProblemFormGroup {
    const problemRawValue = {
      ...this.getFormDefaults(),
      ...problem,
    };
    return new FormGroup<ProblemFormGroupContent>({
      id: new FormControl(
        { value: problemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(problemRawValue.title),
      directory: new FormControl(problemRawValue.directory),
      version: new FormControl(problemRawValue.version),
      tags: new FormControl(problemRawValue.tags),
    });
  }

  getProblem(form: ProblemFormGroup): IProblem | NewProblem {
    return form.getRawValue() as IProblem | NewProblem;
  }

  resetForm(form: ProblemFormGroup, problem: ProblemFormGroupInput): void {
    const problemRawValue = { ...this.getFormDefaults(), ...problem };
    form.reset(
      {
        ...problemRawValue,
        id: { value: problemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProblemFormDefaults {
    return {
      id: null,
    };
  }
}
