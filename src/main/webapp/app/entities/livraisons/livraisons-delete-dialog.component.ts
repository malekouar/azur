import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILivraisons } from 'app/shared/model/livraisons.model';
import { LivraisonsService } from './livraisons.service';

@Component({
  selector: 'jhi-livraisons-delete-dialog',
  templateUrl: './livraisons-delete-dialog.component.html'
})
export class LivraisonsDeleteDialogComponent {
  livraisons: ILivraisons;

  constructor(
    protected livraisonsService: LivraisonsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.livraisonsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'livraisonsListModification',
        content: 'Deleted an livraisons'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-livraisons-delete-popup',
  template: ''
})
export class LivraisonsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ livraisons }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LivraisonsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.livraisons = livraisons;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/livraisons', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/livraisons', { outlets: { popup: null } }]);
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
