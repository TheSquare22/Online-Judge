import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TagCollectionTagFormService, TagCollectionTagFormGroup } from './tag-collection-tag-form.service';
import { ITagCollectionTag } from '../tag-collection-tag.model';
import { TagCollectionTagService } from '../service/tag-collection-tag.service';
import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { TagCollectionService } from 'app/entities/tag-collection/service/tag-collection.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

@Component({
  selector: 'jhi-tag-collection-tag-update',
  templateUrl: './tag-collection-tag-update.component.html',
})
export class TagCollectionTagUpdateComponent implements OnInit {
  isSaving = false;
  tagCollectionTag: ITagCollectionTag | null = null;

  tagCollectionsSharedCollection: ITagCollection[] = [];
  tagsSharedCollection: ITag[] = [];

  editForm: TagCollectionTagFormGroup = this.tagCollectionTagFormService.createTagCollectionTagFormGroup();

  constructor(
    protected tagCollectionTagService: TagCollectionTagService,
    protected tagCollectionTagFormService: TagCollectionTagFormService,
    protected tagCollectionService: TagCollectionService,
    protected tagService: TagService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTagCollection = (o1: ITagCollection | null, o2: ITagCollection | null): boolean =>
    this.tagCollectionService.compareTagCollection(o1, o2);

  compareTag = (o1: ITag | null, o2: ITag | null): boolean => this.tagService.compareTag(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagCollectionTag }) => {
      this.tagCollectionTag = tagCollectionTag;
      if (tagCollectionTag) {
        this.updateForm(tagCollectionTag);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tagCollectionTag = this.tagCollectionTagFormService.getTagCollectionTag(this.editForm);
    if (tagCollectionTag.id !== null) {
      this.subscribeToSaveResponse(this.tagCollectionTagService.update(tagCollectionTag));
    } else {
      this.subscribeToSaveResponse(this.tagCollectionTagService.create(tagCollectionTag));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITagCollectionTag>>): void {
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

  protected updateForm(tagCollectionTag: ITagCollectionTag): void {
    this.tagCollectionTag = tagCollectionTag;
    this.tagCollectionTagFormService.resetForm(this.editForm, tagCollectionTag);

    this.tagCollectionsSharedCollection = this.tagCollectionService.addTagCollectionToCollectionIfMissing<ITagCollection>(
      this.tagCollectionsSharedCollection,
      tagCollectionTag.collection
    );
    this.tagsSharedCollection = this.tagService.addTagToCollectionIfMissing<ITag>(this.tagsSharedCollection, tagCollectionTag.tag);
  }

  protected loadRelationshipsOptions(): void {
    this.tagCollectionService
      .query()
      .pipe(map((res: HttpResponse<ITagCollection[]>) => res.body ?? []))
      .pipe(
        map((tagCollections: ITagCollection[]) =>
          this.tagCollectionService.addTagCollectionToCollectionIfMissing<ITagCollection>(tagCollections, this.tagCollectionTag?.collection)
        )
      )
      .subscribe((tagCollections: ITagCollection[]) => (this.tagCollectionsSharedCollection = tagCollections));

    this.tagService
      .query()
      .pipe(map((res: HttpResponse<ITag[]>) => res.body ?? []))
      .pipe(map((tags: ITag[]) => this.tagService.addTagToCollectionIfMissing<ITag>(tags, this.tagCollectionTag?.tag)))
      .subscribe((tags: ITag[]) => (this.tagsSharedCollection = tags));
  }
}
