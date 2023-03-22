import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TagCollectionTagService } from '../service/tag-collection-tag.service';

import { TagCollectionTagComponent } from './tag-collection-tag.component';

describe('TagCollectionTag Management Component', () => {
  let comp: TagCollectionTagComponent;
  let fixture: ComponentFixture<TagCollectionTagComponent>;
  let service: TagCollectionTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'tag-collection-tag', component: TagCollectionTagComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [TagCollectionTagComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(TagCollectionTagComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagCollectionTagComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TagCollectionTagService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.tagCollectionTags?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to tagCollectionTagService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTagCollectionTagIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTagCollectionTagIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
