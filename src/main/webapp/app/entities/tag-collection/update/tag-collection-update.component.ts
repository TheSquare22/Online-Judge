import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TagCollectionFormService, TagCollectionFormGroup } from './tag-collection-form.service';
import { ITagCollection } from '../tag-collection.model';
import { TagCollectionService } from '../service/tag-collection.service';

@Component({
  selector: 'jhi-tag-collection-update',
  templateUrl: './tag-collection-update.component.html',
})
export class TagCollectionUpdateComponent implements OnInit {
  isSaving = false;
  tagCollection: ITagCollection | null = null;

  editForm: TagCollectionFormGroup = this.tagCollectionFormService.createTagCollectionFormGroup();

  constructor(
    protected tagCollectionService: TagCollectionService,
    protected tagCollectionFormService: TagCollectionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagCollection }) => {
      this.tagCollection = tagCollection;
      if (tagCollection) {
        this.updateForm(tagCollection);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tagCollection = this.tagCollectionFormService.getTagCollection(this.editForm);
    if (tagCollection.id !== null) {
      this.subscribeToSaveResponse(this.tagCollectionService.update(tagCollection));
    } else {
      this.subscribeToSaveResponse(this.tagCollectionService.create(tagCollection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITagCollection>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tagCollection: ITagCollection): void {
    this.tagCollection = tagCollection;
    this.tagCollectionFormService.resetForm(this.editForm, tagCollection);
  }
}
