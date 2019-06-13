import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDossier, Dossier } from 'app/shared/model/dossier.model';
import { DossierService } from './dossier.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';

@Component({
  selector: 'jhi-dossier-update',
  templateUrl: './dossier-update.component.html'
})
export class DossierUpdateComponent implements OnInit {
  isSaving: boolean;

  clients: IClient[];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required, Validators.maxLength(45)]],
    dateDebut: [null, [Validators.required]],
    responsable: [null, [Validators.required, Validators.maxLength(45)]],
    etat: [null, [Validators.required, Validators.maxLength(45)]],
    urlAzimut: [null, [Validators.maxLength(255)]],
    urlRedmine: [null, [Validators.maxLength(255)]],
    urlAkuiteo: [null, [Validators.maxLength(255)]],
    dateFin: [null, [Validators.required]],
    clientId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected dossierService: DossierService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.updateForm(dossier);
    });
    this.clientService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClient[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClient[]>) => response.body)
      )
      .subscribe((res: IClient[]) => (this.clients = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(dossier: IDossier) {
    this.editForm.patchValue({
      id: dossier.id,
      type: dossier.type,
      dateDebut: dossier.dateDebut != null ? dossier.dateDebut.format(DATE_TIME_FORMAT) : null,
      responsable: dossier.responsable,
      etat: dossier.etat,
      urlAzimut: dossier.urlAzimut,
      urlRedmine: dossier.urlRedmine,
      urlAkuiteo: dossier.urlAkuiteo,
      dateFin: dossier.dateFin != null ? dossier.dateFin.format(DATE_TIME_FORMAT) : null,
      clientId: dossier.clientId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const dossier = this.createFromForm();
    if (dossier.id !== undefined) {
      this.subscribeToSaveResponse(this.dossierService.update(dossier));
    } else {
      this.subscribeToSaveResponse(this.dossierService.create(dossier));
    }
  }

  private createFromForm(): IDossier {
    const entity = {
      ...new Dossier(),
      id: this.editForm.get(['id']).value,
      type: this.editForm.get(['type']).value,
      dateDebut:
        this.editForm.get(['dateDebut']).value != null ? moment(this.editForm.get(['dateDebut']).value, DATE_TIME_FORMAT) : undefined,
      responsable: this.editForm.get(['responsable']).value,
      etat: this.editForm.get(['etat']).value,
      urlAzimut: this.editForm.get(['urlAzimut']).value,
      urlRedmine: this.editForm.get(['urlRedmine']).value,
      urlAkuiteo: this.editForm.get(['urlAkuiteo']).value,
      dateFin: this.editForm.get(['dateFin']).value != null ? moment(this.editForm.get(['dateFin']).value, DATE_TIME_FORMAT) : undefined,
      clientId: this.editForm.get(['clientId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossier>>) {
    result.subscribe((res: HttpResponse<IDossier>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackClientById(index: number, item: IClient) {
    return item.id;
  }
}
