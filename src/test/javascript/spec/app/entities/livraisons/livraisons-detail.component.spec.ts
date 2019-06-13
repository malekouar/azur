/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AzursupportwebtoolTestModule } from '../../../test.module';
import { LivraisonsDetailComponent } from 'app/entities/livraisons/livraisons-detail.component';
import { Livraisons } from 'app/shared/model/livraisons.model';

describe('Component Tests', () => {
  describe('Livraisons Management Detail Component', () => {
    let comp: LivraisonsDetailComponent;
    let fixture: ComponentFixture<LivraisonsDetailComponent>;
    const route = ({ data: of({ livraisons: new Livraisons(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AzursupportwebtoolTestModule],
        declarations: [LivraisonsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LivraisonsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LivraisonsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.livraisons).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
