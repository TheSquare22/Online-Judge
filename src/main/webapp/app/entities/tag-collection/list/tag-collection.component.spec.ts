import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TagCollectionService } from '../service/tag-collection.service';

import { TagCollectionComponent } from './tag-collection.component';

describe('TagCollection Management Component', () => {
  let comp: TagCollectionComponent;
  let fixture: ComponentFixture<TagCollectionComponent>;
  let service: TagCollectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'tag-collection', component: TagCollectionComponent }]), HttpClientTestingModule],
      declarations: [TagCollectionComponent],
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
      .overrideTemplate(TagCollectionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagCollectionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TagCollectionService);

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
    expect(comp.tagCollections?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to tagCollectionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTagCollectionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTagCollectionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
