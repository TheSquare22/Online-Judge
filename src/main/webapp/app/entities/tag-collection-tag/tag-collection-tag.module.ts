import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TagCollectionTagComponent } from './list/tag-collection-tag.component';
import { TagCollectionTagDetailComponent } from './detail/tag-collection-tag-detail.component';
import { TagCollectionTagUpdateComponent } from './update/tag-collection-tag-update.component';
import { TagCollectionTagDeleteDialogComponent } from './delete/tag-collection-tag-delete-dialog.component';
import { TagCollectionTagRoutingModule } from './route/tag-collection-tag-routing.module';

@NgModule({
  imports: [SharedModule, TagCollectionTagRoutingModule],
  declarations: [
    TagCollectionTagComponent,
    TagCollectionTagDetailComponent,
    TagCollectionTagUpdateComponent,
    TagCollectionTagDeleteDialogComponent,
  ],
})
export class TagCollectionTagModule {}
