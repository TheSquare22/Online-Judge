import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITagCollectionTag } from '../tag-collection-tag.model';
import { TagCollectionTagService } from '../service/tag-collection-tag.service';

@Injectable({ providedIn: 'root' })
export class TagCollectionTagRoutingResolveService implements Resolve<ITagCollectionTag | null> {
  constructor(protected service: TagCollectionTagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITagCollectionTag | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tagCollectionTag: HttpResponse<ITagCollectionTag>) => {
          if (tagCollectionTag.body) {
            return of(tagCollectionTag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
