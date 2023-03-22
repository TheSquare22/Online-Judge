import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TagCollectionComponent } from './list/tag-collection.component';
import { TagCollectionDetailComponent } from './detail/tag-collection-detail.component';
import { TagCollectionUpdateComponent } from './update/tag-collection-update.component';
import { TagCollectionDeleteDialogComponent } from './delete/tag-collection-delete-dialog.component';
import { TagCollectionRoutingModule } from './route/tag-collection-routing.module';

@NgModule({
  imports: [SharedModule, TagCollectionRoutingModule],
  declarations: [TagCollectionComponent, TagCollectionDetailComponent, TagCollectionUpdateComponent, TagCollectionDeleteDialogComponent],
})
export class TagCollectionModule {}
