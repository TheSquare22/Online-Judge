import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITagCollection } from '../tag-collection.model';
import { TagCollectionService } from '../service/tag-collection.service';

@Injectable({ providedIn: 'root' })
export class TagCollectionRoutingResolveService implements Resolve<ITagCollection | null> {
  constructor(protected service: TagCollectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITagCollection | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tagCollection: HttpResponse<ITagCollection>) => {
          if (tagCollection.body) {
            return of(tagCollection.body);
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
