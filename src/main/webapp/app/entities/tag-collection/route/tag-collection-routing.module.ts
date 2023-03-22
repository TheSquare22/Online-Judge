import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TagCollectionComponent } from '../list/tag-collection.component';
import { TagCollectionDetailComponent } from '../detail/tag-collection-detail.component';
import { TagCollectionUpdateComponent } from '../update/tag-collection-update.component';
import { TagCollectionRoutingResolveService } from './tag-collection-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tagCollectionRoute: Routes = [
  {
    path: '',
    component: TagCollectionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TagCollectionDetailComponent,
    resolve: {
      tagCollection: TagCollectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TagCollectionUpdateComponent,
    resolve: {
      tagCollection: TagCollectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TagCollectionUpdateComponent,
    resolve: {
      tagCollection: TagCollectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tagCollectionRoute)],
  exports: [RouterModule],
})
export class TagCollectionRoutingModule {}
