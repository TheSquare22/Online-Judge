import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TagCollectionDetailComponent } from './tag-collection-detail.component';

describe('TagCollection Management Detail Component', () => {
  let comp: TagCollectionDetailComponent;
  let fixture: ComponentFixture<TagCollectionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TagCollectionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tagCollection: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TagCollectionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TagCollectionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tagCollection on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tagCollection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
