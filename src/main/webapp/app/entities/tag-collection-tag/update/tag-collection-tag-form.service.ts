import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITagCollectionTag, NewTagCollectionTag } from '../tag-collection-tag.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITagCollectionTag for edit and NewTagCollectionTagFormGroupInput for create.
 */
type TagCollectionTagFormGroupInput = ITagCollectionTag | PartialWithRequiredKeyOf<NewTagCollectionTag>;

type TagCollectionTagFormDefaults = Pick<NewTagCollectionTag, 'id'>;

type TagCollectionTagFormGroupContent = {
  id: FormControl<ITagCollectionTag['id'] | NewTagCollectionTag['id']>;
  collection: FormControl<ITagCollectionTag['collection']>;
  tag: FormControl<ITagCollectionTag['tag']>;
};

export type TagCollectionTagFormGroup = FormGroup<TagCollectionTagFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagCollectionTagFormService {
  createTagCollectionTagFormGroup(tagCollectionTag: TagCollectionTagFormGroupInput = { id: null }): TagCollectionTagFormGroup {
    const tagCollectionTagRawValue = {
      ...this.getFormDefaults(),
      ...tagCollectionTag,
    };
    return new FormGroup<TagCollectionTagFormGroupContent>({
      id: new FormControl(
        { value: tagCollectionTagRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      collection: new FormControl(tagCollectionTagRawValue.collection),
      tag: new FormControl(tagCollectionTagRawValue.tag),
    });
  }

  getTagCollectionTag(form: TagCollectionTagFormGroup): ITagCollectionTag | NewTagCollectionTag {
    return form.getRawValue() as ITagCollectionTag | NewTagCollectionTag;
  }

  resetForm(form: TagCollectionTagFormGroup, tagCollectionTag: TagCollectionTagFormGroupInput): void {
    const tagCollectionTagRawValue = { ...this.getFormDefaults(), ...tagCollectionTag };
    form.reset(
      {
        ...tagCollectionTagRawValue,
        id: { value: tagCollectionTagRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TagCollectionTagFormDefaults {
    return {
      id: null,
    };
  }
}
