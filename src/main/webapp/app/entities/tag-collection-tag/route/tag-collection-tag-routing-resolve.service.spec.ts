import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITagCollectionTag } from '../tag-collection-tag.model';
import { TagCollectionTagService } from '../service/tag-collection-tag.service';

import { TagCollectionTagRoutingResolveService } from './tag-collection-tag-routing-resolve.service';

describe('TagCollectionTag routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TagCollectionTagRoutingResolveService;
  let service: TagCollectionTagService;
  let resultTagCollectionTag: ITagCollectionTag | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TagCollectionTagRoutingResolveService);
    service = TestBed.inject(TagCollectionTagService);
    resultTagCollectionTag = undefined;
  });

  describe('resolve', () => {
    it('should return ITagCollectionTag returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTagCollectionTag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTagCollectionTag).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTagCollectionTag = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTagCollectionTag).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITagCollectionTag>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTagCollectionTag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTagCollectionTag).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
