/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AzursupportwebtoolTestModule } from '../../../test.module';
import { InterventionDeleteDialogComponent } from 'app/entities/intervention/intervention-delete-dialog.component';
import { InterventionService } from 'app/entities/intervention/intervention.service';

describe('Component Tests', () => {
  describe('Intervention Management Delete Component', () => {
    let comp: InterventionDeleteDialogComponent;
    let fixture: ComponentFixture<InterventionDeleteDialogComponent>;
    let service: InterventionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AzursupportwebtoolTestModule],
        declarations: [InterventionDeleteDialogComponent]
      })
        .overrideTemplate(InterventionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterventionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterventionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
