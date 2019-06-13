/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { AzursupportwebtoolTestModule } from '../../../test.module';
import { LivraisonsUpdateComponent } from 'app/entities/livraisons/livraisons-update.component';
import { LivraisonsService } from 'app/entities/livraisons/livraisons.service';
import { Livraisons } from 'app/shared/model/livraisons.model';

describe('Component Tests', () => {
  describe('Livraisons Management Update Component', () => {
    let comp: LivraisonsUpdateComponent;
    let fixture: ComponentFixture<LivraisonsUpdateComponent>;
    let service: LivraisonsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AzursupportwebtoolTestModule],
        declarations: [LivraisonsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LivraisonsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LivraisonsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LivraisonsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Livraisons(123);
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
        const entity = new Livraisons();
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
