import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITagCollection, NewTagCollection } from '../tag-collection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITagCollection for edit and NewTagCollectionFormGroupInput for create.
 */
type TagCollectionFormGroupInput = ITagCollection | PartialWithRequiredKeyOf<NewTagCollection>;

type TagCollectionFormDefaults = Pick<NewTagCollection, 'id'>;

type TagCollectionFormGroupContent = {
  id: FormControl<ITagCollection['id'] | NewTagCollection['id']>;
};

export type TagCollectionFormGroup = FormGroup<TagCollectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagCollectionFormService {
  createTagCollectionFormGroup(tagCollection: TagCollectionFormGroupInput = { id: null }): TagCollectionFormGroup {
    const tagCollectionRawValue = {
      ...this.getFormDefaults(),
      ...tagCollection,
    };
    return new FormGroup<TagCollectionFormGroupContent>({
      id: new FormControl(
        { value: tagCollectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getTagCollection(form: TagCollectionFormGroup): ITagCollection | NewTagCollection {
    if (form.controls.id.disabled) {
      // form.value returns id with null value for FormGroup with only one FormControl
      return {};
    }
    return form.getRawValue() as ITagCollection | NewTagCollection;
  }

  resetForm(form: TagCollectionFormGroup, tagCollection: TagCollectionFormGroupInput): void {
    const tagCollectionRawValue = { ...this.getFormDefaults(), ...tagCollection };
    form.reset(
      {
        ...tagCollectionRawValue,
        id: { value: tagCollectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TagCollectionFormDefaults {
    return {
      id: null,
    };
  }
}
