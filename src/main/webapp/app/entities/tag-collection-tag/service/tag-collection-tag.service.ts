import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITagCollectionTag, NewTagCollectionTag } from '../tag-collection-tag.model';

export type PartialUpdateTagCollectionTag = Partial<ITagCollectionTag> & Pick<ITagCollectionTag, 'id'>;

export type EntityResponseType = HttpResponse<ITagCollectionTag>;
export type EntityArrayResponseType = HttpResponse<ITagCollectionTag[]>;

@Injectable({ providedIn: 'root' })
export class TagCollectionTagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tag-collection-tags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tagCollectionTag: NewTagCollectionTag): Observable<EntityResponseType> {
    return this.http.post<ITagCollectionTag>(this.resourceUrl, tagCollectionTag, { observe: 'response' });
  }

  update(tagCollectionTag: ITagCollectionTag): Observable<EntityResponseType> {
    return this.http.put<ITagCollectionTag>(
      `${this.resourceUrl}/${this.getTagCollectionTagIdentifier(tagCollectionTag)}`,
      tagCollectionTag,
      { observe: 'response' }
    );
  }

  partialUpdate(tagCollectionTag: PartialUpdateTagCollectionTag): Observable<EntityResponseType> {
    return this.http.patch<ITagCollectionTag>(
      `${this.resourceUrl}/${this.getTagCollectionTagIdentifier(tagCollectionTag)}`,
      tagCollectionTag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITagCollectionTag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITagCollectionTag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTagCollectionTagIdentifier(tagCollectionTag: Pick<ITagCollectionTag, 'id'>): number {
    return tagCollectionTag.id;
  }

  compareTagCollectionTag(o1: Pick<ITagCollectionTag, 'id'> | null, o2: Pick<ITagCollectionTag, 'id'> | null): boolean {
    return o1 && o2 ? this.getTagCollectionTagIdentifier(o1) === this.getTagCollectionTagIdentifier(o2) : o1 === o2;
  }

  addTagCollectionTagToCollectionIfMissing<Type extends Pick<ITagCollectionTag, 'id'>>(
    tagCollectionTagCollection: Type[],
    ...tagCollectionTagsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tagCollectionTags: Type[] = tagCollectionTagsToCheck.filter(isPresent);
    if (tagCollectionTags.length > 0) {
      const tagCollectionTagCollectionIdentifiers = tagCollectionTagCollection.map(
        tagCollectionTagItem => this.getTagCollectionTagIdentifier(tagCollectionTagItem)!
      );
      const tagCollectionTagsToAdd = tagCollectionTags.filter(tagCollectionTagItem => {
        const tagCollectionTagIdentifier = this.getTagCollectionTagIdentifier(tagCollectionTagItem);
        if (tagCollectionTagCollectionIdentifiers.includes(tagCollectionTagIdentifier)) {
          return false;
        }
        tagCollectionTagCollectionIdentifiers.push(tagCollectionTagIdentifier);
        return true;
      });
      return [...tagCollectionTagsToAdd, ...tagCollectionTagCollection];
    }
    return tagCollectionTagCollection;
  }
}
