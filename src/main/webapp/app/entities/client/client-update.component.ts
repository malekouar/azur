import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IClient, Client } from 'app/shared/model/client.model';
import { ClientService } from './client.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html'
})
export class ClientUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    raisonSociale: [null, [Validators.required, Validators.maxLength(255)]],
    contact: [null, [Validators.required, Validators.maxLength(45)]],
    tel: [null, [Validators.maxLength(45)]],
    mobile: [null, [Validators.maxLength(45)]],
    email: [null, [Validators.maxLength(100)]]
  });

  constructor(protected clientService: ClientService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ client }) => {
      this.updateForm(client);
    });
  }

  updateForm(client: IClient) {
    this.editForm.patchValue({
      id: client.id,
      raisonSociale: client.raisonSociale,
      contact: client.contact,
      tel: client.tel,
      mobile: client.mobile,
      email: client.email
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  private createFromForm(): IClient {
    const entity = {
      ...new Client(),
      id: this.editForm.get(['id']).value,
      raisonSociale: this.editForm.get(['raisonSociale']).value,
      contact: this.editForm.get(['contact']).value,
      tel: this.editForm.get(['tel']).value,
      mobile: this.editForm.get(['mobile']).value,
      email: this.editForm.get(['email']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>) {
    result.subscribe((res: HttpResponse<IClient>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
