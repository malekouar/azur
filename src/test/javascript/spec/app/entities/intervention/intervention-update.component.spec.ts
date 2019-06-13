/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { AzursupportwebtoolTestModule } from '../../../test.module';
import { InterventionUpdateComponent } from 'app/entities/intervention/intervention-update.component';
import { InterventionService } from 'app/entities/intervention/intervention.service';
import { Intervention } from 'app/shared/model/intervention.model';

describe('Component Tests', () => {
  describe('Intervention Management Update Component', () => {
    let comp: InterventionUpdateComponent;
    let fixture: ComponentFixture<InterventionUpdateComponent>;
    let service: InterventionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AzursupportwebtoolTestModule],
        declarations: [InterventionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InterventionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterventionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterventionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Intervention(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Intervention();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
