import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITagCollection } from '../tag-collection.model';
import { TagCollectionService } from '../service/tag-collection.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tag-collection-delete-dialog.component.html',
})
export class TagCollectionDeleteDialogComponent {
  tagCollection?: ITagCollection;

  constructor(protected tagCollectionService: TagCollectionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tagCollectionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
