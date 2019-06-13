import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IServeur, Serveur } from 'app/shared/model/serveur.model';
import { ServeurService } from './serveur.service';
import { IConfig } from 'app/shared/model/config.model';
import { ConfigService } from 'app/entities/config';

@Component({
  selector: 'jhi-serveur-update',
  templateUrl: './serveur-update.component.html'
})
export class ServeurUpdateComponent implements OnInit {
  isSaving: boolean;

  configs: IConfig[];

  editForm = this.fb.group({
    id: [],
    serveurType: [null, [Validators.maxLength(45)]],
    serveurNom: [null, [Validators.maxLength(45)]],
    serveurIp: [null, [Validators.maxLength(45)]],
    login: [null, [Validators.maxLength(45)]],
    password: [null, [Validators.maxLength(45)]],
    configId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected serveurService: ServeurService,
    protected configService: ConfigService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ serveur }) => {
      this.updateForm(serveur);
    });
    this.configService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IConfig[]>) => mayBeOk.ok),
        map((response: HttpResponse<IConfig[]>) => response.body)
      )
      .subscribe((res: IConfig[]) => (this.configs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(serveur: IServeur) {
    this.editForm.patchValue({
      id: serveur.id,
      serveurType: serveur.serveurType,
      serveurNom: serveur.serveurNom,
      serveurIp: serveur.serveurIp,
      login: serveur.login,
      password: serveur.password,
      configId: serveur.configId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const serveur = this.createFromForm();
    if (serveur.id !== undefined) {
      this.subscribeToSaveResponse(this.serveurService.update(serveur));
    } else {
      this.subscribeToSaveResponse(this.serveurService.create(serveur));
    }
  }

  private createFromForm(): IServeur {
    const entity = {
      ...new Serveur(),
      id: this.editForm.get(['id']).value,
      serveurType: this.editForm.get(['serveurType']).value,
      serveurNom: this.editForm.get(['serveurNom']).value,
      serveurIp: this.editForm.get(['serveurIp']).value,
      login: this.editForm.get(['login']).value,
      password: this.editForm.get(['password']).value,
      configId: this.editForm.get(['configId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServeur>>) {
    result.subscribe((res: HttpResponse<IServeur>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackConfigById(index: number, item: IConfig) {
    return item.id;
  }
}
