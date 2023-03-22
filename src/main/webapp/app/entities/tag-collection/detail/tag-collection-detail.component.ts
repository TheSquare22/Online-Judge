import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITagCollection } from '../tag-collection.model';

@Component({
  selector: 'jhi-tag-collection-detail',
  templateUrl: './tag-collection-detail.component.html',
})
export class TagCollectionDetailComponent implements OnInit {
  tagCollection: ITagCollection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tagCollection }) => {
      this.tagCollection = tagCollection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
