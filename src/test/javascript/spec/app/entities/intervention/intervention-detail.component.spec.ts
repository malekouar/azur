/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AzursupportwebtoolTestModule } from '../../../test.module';
import { InterventionDetailComponent } from 'app/entities/intervention/intervention-detail.component';
import { Intervention } from 'app/shared/model/intervention.model';

describe('Component Tests', () => {
  describe('Intervention Management Detail Component', () => {
    let comp: InterventionDetailComponent;
    let fixture: ComponentFixture<InterventionDetailComponent>;
    const route = ({ data: of({ intervention: new Intervention(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AzursupportwebtoolTestModule],
        declarations: [InterventionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InterventionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterventionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.intervention).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
