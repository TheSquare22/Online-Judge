import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITagCollectionTag } from '../tag-collection-tag.model';
import { TagCollectionTagService } from '../service/tag-collection-tag.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tag-collection-tag-delete-dialog.component.html',
})
export class TagCollectionTagDeleteDialogComponent {
  tagCollectionTag?: ITagCollectionTag;

  constructor(protected tagCollectionTagService: TagCollectionTagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagCollectionTagService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
