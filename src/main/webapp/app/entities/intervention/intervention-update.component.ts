import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IIntervention, Intervention } from 'app/shared/model/intervention.model';
import { InterventionService } from './intervention.service';
import { IDossier } from 'app/shared/model/dossier.model';
import { DossierService } from 'app/entities/dossier';

@Component({
  selector: 'jhi-intervention-update',
  templateUrl: './intervention-update.component.html'
})
export class InterventionUpdateComponent implements OnInit {
  isSaving: boolean;

  dossiers: IDossier[];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required, Validators.maxLength(45)]],
    dateDebut: [null, [Validators.required]],
    dateFin: [],
    responsable: [null, [Validators.required, Validators.maxLength(45)]],
    etat: [null, [Validators.required, Validators.maxLength(45)]],
    description: [null, [Validators.maxLength(255)]],
    detail: [null, [Validators.maxLength(255)]],
    dossierId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected interventionService: InterventionService,
    protected dossierService: DossierService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ intervention }) => {
      this.updateForm(intervention);
    });
    this.dossierService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDossier[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDossier[]>) => response.body)
      )
      .subscribe((res: IDossier[]) => (this.dossiers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(intervention: IIntervention) {
    this.editForm.patchValue({
      id: intervention.id,
      type: intervention.type,
      dateDebut: intervention.dateDebut != null ? intervention.dateDebut.format(DATE_TIME_FORMAT) : null,
      dateFin: intervention.dateFin != null ? intervention.dateFin.format(DATE_TIME_FORMAT) : null,
      responsable: intervention.responsable,
      etat: intervention.etat,
      description: intervention.description,
      detail: intervention.detail,
      dossierId: intervention.dossierId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const intervention = this.createFromForm();
    if (intervention.id !== undefined) {
      this.subscribeToSaveResponse(this.interventionService.update(intervention));
    } else {
      this.subscribeToSaveResponse(this.interventionService.create(intervention));
    }
  }

  private createFromForm(): IIntervention {
    const entity = {
      ...new Intervention(),
      id: this.editForm.get(['id']).value,
      type: this.editForm.get(['type']).value,
      dateDebut:
        this.editForm.get(['dateDebut']).value != null ? moment(this.editForm.get(['dateDebut']).value, DATE_TIME_FORMAT) : undefined,
      dateFin: this.editForm.get(['dateFin']).value != null ? moment(this.editForm.get(['dateFin']).value, DATE_TIME_FORMAT) : undefined,
      responsable: this.editForm.get(['responsable']).value,
      etat: this.editForm.get(['etat']).value,
      description: this.editForm.get(['description']).value,
      detail: this.editForm.get(['detail']).value,
      dossierId: this.editForm.get(['dossierId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntervention>>) {
    result.subscribe((res: HttpResponse<IIntervention>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackDossierById(index: number, item: IDossier) {
    return item.id;
  }
}
