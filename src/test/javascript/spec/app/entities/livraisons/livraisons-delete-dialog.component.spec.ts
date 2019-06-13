/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AzursupportwebtoolTestModule } from '../../../test.module';
import { LivraisonsDeleteDialogComponent } from 'app/entities/livraisons/livraisons-delete-dialog.component';
import { LivraisonsService } from 'app/entities/livraisons/livraisons.service';

describe('Component Tests', () => {
  describe('Livraisons Management Delete Component', () => {
    let comp: LivraisonsDeleteDialogComponent;
    let fixture: ComponentFixture<LivraisonsDeleteDialogComponent>;
    let service: LivraisonsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AzursupportwebtoolTestModule],
        declarations: [LivraisonsDeleteDialogComponent]
      })
        .overrideTemplate(LivraisonsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LivraisonsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LivraisonsService);
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
