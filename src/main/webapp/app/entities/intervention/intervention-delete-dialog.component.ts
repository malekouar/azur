import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIntervention } from 'app/shared/model/intervention.model';
import { InterventionService } from './intervention.service';

@Component({
  selector: 'jhi-intervention-delete-dialog',
  templateUrl: './intervention-delete-dialog.component.html'
})
export class InterventionDeleteDialogComponent {
  intervention: IIntervention;

  constructor(
    protected interventionService: InterventionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.interventionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'interventionListModification',
        content: 'Deleted an intervention'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-intervention-delete-popup',
  template: ''
})
export class InterventionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ intervention }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(InterventionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.intervention = intervention;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/intervention', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/intervention', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
