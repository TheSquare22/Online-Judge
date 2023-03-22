import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITagCollection, NewTagCollection } from '../tag-collection.model';

export type PartialUpdateTagCollection = Partial<ITagCollection> & Pick<ITagCollection, 'id'>;

export type EntityResponseType = HttpResponse<ITagCollection>;
export type EntityArrayResponseType = HttpResponse<ITagCollection[]>;

@Injectable({ providedIn: 'root' })
export class TagCollectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tag-collections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tagCollection: NewTagCollection): Observable<EntityResponseType> {
    return this.http.post<ITagCollection>(this.resourceUrl, tagCollection, { observe: 'response' });
  }

  update(tagCollection: ITagCollection): Observable<EntityResponseType> {
    return this.http.put<ITagCollection>(`${this.resourceUrl}/${this.getTagCollectionIdentifier(tagCollection)}`, tagCollection, {
      observe: 'response',
    });
  }

  partialUpdate(tagCollection: PartialUpdateTagCollection): Observable<EntityResponseType> {
    return this.http.patch<ITagCollection>(`${this.resourceUrl}/${this.getTagCollectionIdentifier(tagCollection)}`, tagCollection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITagCollection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITagCollection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTagCollectionIdentifier(tagCollection: Pick<ITagCollection, 'id'>): number {
    return tagCollection.id;
  }

  compareTagCollection(o1: Pick<ITagCollection, 'id'> | null, o2: Pick<ITagCollection, 'id'> | null): boolean {
    return o1 && o2 ? this.getTagCollectionIdentifier(o1) === this.getTagCollectionIdentifier(o2) : o1 === o2;
  }

  addTagCollectionToCollectionIfMissing<Type extends Pick<ITagCollection, 'id'>>(
    tagCollectionCollection: Type[],
    ...tagCollectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tagCollections: Type[] = tagCollectionsToCheck.filter(isPresent);
    if (tagCollections.length > 0) {
      const tagCollectionCollectionIdentifiers = tagCollectionCollection.map(
        tagCollectionItem => this.getTagCollectionIdentifier(tagCollectionItem)!
      );
      const tagCollectionsToAdd = tagCollections.filter(tagCollectionItem => {
        const tagCollectionIdentifier = this.getTagCollectionIdentifier(tagCollectionItem);
        if (tagCollectionCollectionIdentifiers.includes(tagCollectionIdentifier)) {
          return false;
        }
        tagCollectionCollectionIdentifiers.push(tagCollectionIdentifier);
        return true;
      });
      return [...tagCollectionsToAdd, ...tagCollectionCollection];
    }
    return tagCollectionCollection;
  }
}
