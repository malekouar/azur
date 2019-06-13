import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ILivraisons, Livraisons } from 'app/shared/model/livraisons.model';
import { LivraisonsService } from './livraisons.service';
import { IIntervention } from 'app/shared/model/intervention.model';
import { InterventionService } from 'app/entities/intervention';

@Component({
  selector: 'jhi-livraisons-update',
  templateUrl: './livraisons-update.component.html'
})
export class LivraisonsUpdateComponent implements OnInit {
  isSaving: boolean;

  interventions: IIntervention[];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required, Validators.maxLength(45)]],
    dateLivraison: [null, [Validators.required]],
    responsable: [null, [Validators.required, Validators.maxLength(45)]],
    etat: [null, [Validators.required, Validators.maxLength(45)]],
    nomPackage: [null, [Validators.required, Validators.maxLength(255)]],
    idSvn: [null, [Validators.required]],
    description: [null, [Validators.maxLength(255)]],
    detail: [null, [Validators.maxLength(255)]],
    interventionId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected livraisonsService: LivraisonsService,
    protected interventionService: InterventionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ livraisons }) => {
      this.updateForm(livraisons);
    });
    this.interventionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIntervention[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIntervention[]>) => response.body)
      )
      .subscribe((res: IIntervention[]) => (this.interventions = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(livraisons: ILivraisons) {
    this.editForm.patchValue({
      id: livraisons.id,
      type: livraisons.type,
      dateLivraison: livraisons.dateLivraison != null ? livraisons.dateLivraison.format(DATE_TIME_FORMAT) : null,
      responsable: livraisons.responsable,
      etat: livraisons.etat,
      nomPackage: livraisons.nomPackage,
      idSvn: livraisons.idSvn,
      description: livraisons.description,
      detail: livraisons.detail,
      interventionId: livraisons.interventionId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const livraisons = this.createFromForm();
    if (livraisons.id !== undefined) {
      this.subscribeToSaveResponse(this.livraisonsService.update(livraisons));
    } else {
      this.subscribeToSaveResponse(this.livraisonsService.create(livraisons));
    }
  }

  private createFromForm(): ILivraisons {
    const entity = {
      ...new Livraisons(),
      id: this.editForm.get(['id']).value,
      type: this.editForm.get(['type']).value,
      dateLivraison:
        this.editForm.get(['dateLivraison']).value != null
          ? moment(this.editForm.get(['dateLivraison']).value, DATE_TIME_FORMAT)
          : undefined,
      responsable: this.editForm.get(['responsable']).value,
      etat: this.editForm.get(['etat']).value,
      nomPackage: this.editForm.get(['nomPackage']).value,
      idSvn: this.editForm.get(['idSvn']).value,
      description: this.editForm.get(['description']).value,
      detail: this.editForm.get(['detail']).value,
      interventionId: this.editForm.get(['interventionId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivraisons>>) {
    result.subscribe((res: HttpResponse<ILivraisons>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackInterventionById(index: number, item: IIntervention) {
    return item.id;
  }
}
