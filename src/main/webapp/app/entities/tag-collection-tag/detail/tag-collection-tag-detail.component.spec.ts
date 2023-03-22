import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TagCollectionTagDetailComponent } from './tag-collection-tag-detail.component';

describe('TagCollectionTag Management Detail Component', () => {
  let comp: TagCollectionTagDetailComponent;
  let fixture: ComponentFixture<TagCollectionTagDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TagCollectionTagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tagCollectionTag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TagCollectionTagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TagCollectionTagDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tagCollectionTag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tagCollectionTag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
