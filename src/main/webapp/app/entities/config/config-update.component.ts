import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IConfig, Config } from 'app/shared/model/config.model';
import { ConfigService } from './config.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client';

@Component({
  selector: 'jhi-config-update',
  templateUrl: './config-update.component.html'
})
export class ConfigUpdateComponent implements OnInit {
  isSaving: boolean;

  clients: IClient[];

  editForm = this.fb.group({
    id: [],
    teamviewerId: [null, [Validators.maxLength(45)]],
    teamviewerPassword: [null, [Validators.maxLength(45)]],
    vpnType: [null, [Validators.maxLength(45)]],
    vpnIp: [null, [Validators.maxLength(45)]],
    vpnLogin: [null, [Validators.maxLength(255)]],
    vpnPassword: [null, [Validators.maxLength(100)]],
    clientId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected configService: ConfigService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ config }) => {
      this.updateForm(config);
    });
    this.clientService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClient[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClient[]>) => response.body)
      )
      .subscribe((res: IClient[]) => (this.clients = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(config: IConfig) {
    this.editForm.patchValue({
      id: config.id,
      teamviewerId: config.teamviewerId,
      teamviewerPassword: config.teamviewerPassword,
      vpnType: config.vpnType,
      vpnIp: config.vpnIp,
      vpnLogin: config.vpnLogin,
      vpnPassword: config.vpnPassword,
      clientId: config.clientId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const config = this.createFromForm();
    if (config.id !== undefined) {
      this.subscribeToSaveResponse(this.configService.update(config));
    } else {
      this.subscribeToSaveResponse(this.configService.create(config));
    }
  }

  private createFromForm(): IConfig {
    const entity = {
      ...new Config(),
      id: this.editForm.get(['id']).value,
      teamviewerId: this.editForm.get(['teamviewerId']).value,
      teamviewerPassword: this.editForm.get(['teamviewerPassword']).value,
      vpnType: this.editForm.get(['vpnType']).value,
      vpnIp: this.editForm.get(['vpnIp']).value,
      vpnLogin: this.editForm.get(['vpnLogin']).value,
      vpnPassword: this.editForm.get(['vpnPassword']).value,
      clientId: this.editForm.get(['clientId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfig>>) {
    result.subscribe((res: HttpResponse<IConfig>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
