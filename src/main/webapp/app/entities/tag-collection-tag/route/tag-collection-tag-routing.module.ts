import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TagCollectionTagComponent } from '../list/tag-collection-tag.component';
import { TagCollectionTagDetailComponent } from '../detail/tag-collection-tag-detail.component';
import { TagCollectionTagUpdateComponent } from '../update/tag-collection-tag-update.component';
import { TagCollectionTagRoutingResolveService } from './tag-collection-tag-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tagCollectionTagRoute: Routes = [
  {
    path: '',
    component: TagCollectionTagComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TagCollectionTagDetailComponent,
    resolve: {
      tagCollectionTag: TagCollectionTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TagCollectionTagUpdateComponent,
    resolve: {
      tagCollectionTag: TagCollectionTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TagCollectionTagUpdateComponent,
    resolve: {
      tagCollectionTag: TagCollectionTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tagCollectionTagRoute)],
  exports: [RouterModule],
})
export class TagCollectionTagRoutingModule {}
