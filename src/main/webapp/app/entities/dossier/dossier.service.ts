import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDossier } from 'app/shared/model/dossier.model';

type EntityResponseType = HttpResponse<IDossier>;
type EntityArrayResponseType = HttpResponse<IDossier[]>;

@Injectable({ providedIn: 'root' })
export class DossierService {
  public resourceUrl = SERVER_API_URL + 'api/dossiers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/dossiers';

  constructor(protected http: HttpClient) {}

  create(dossier: IDossier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dossier);
    return this.http
      .post<IDossier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dossier: IDossier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dossier);
    return this.http
      .put<IDossier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDossier>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDossier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDossier[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(dossier: IDossier): IDossier {
    const copy: IDossier = Object.assign({}, dossier, {
      dateDebut: dossier.dateDebut != null && dossier.dateDebut.isValid() ? dossier.dateDebut.toJSON() : null,
      dateFin: dossier.dateFin != null && dossier.dateFin.isValid() ? dossier.dateFin.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut != null ? moment(res.body.dateDebut) : null;
      res.body.dateFin = res.body.dateFin != null ? moment(res.body.dateFin) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dossier: IDossier) => {
        dossier.dateDebut = dossier.dateDebut != null ? moment(dossier.dateDebut) : null;
        dossier.dateFin = dossier.dateFin != null ? moment(dossier.dateFin) : null;
      });
    }
    return res;
  }
}
