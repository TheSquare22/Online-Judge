import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITagCollectionTag } from '../tag-collection-tag.model';

@Component({
  selector: 'jhi-tag-collection-tag-detail',
  templateUrl: './tag-collection-tag-detail.component.html',
})
export class TagCollectionTagDetailComponent implements OnInit {
  tagCollectionTag: ITagCollectionTag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagCollectionTag }) => {
      this.tagCollectionTag = tagCollectionTag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
