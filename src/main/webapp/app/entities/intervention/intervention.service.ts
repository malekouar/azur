import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIntervention } from 'app/shared/model/intervention.model';

type EntityResponseType = HttpResponse<IIntervention>;
type EntityArrayResponseType = HttpResponse<IIntervention[]>;

@Injectable({ providedIn: 'root' })
export class InterventionService {
  public resourceUrl = SERVER_API_URL + 'api/interventions';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/interventions';

  constructor(protected http: HttpClient) {}

  create(intervention: IIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intervention);
    return this.http
      .post<IIntervention>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(intervention: IIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intervention);
    return this.http
      .put<IIntervention>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIntervention>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIntervention[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIntervention[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(intervention: IIntervention): IIntervention {
    const copy: IIntervention = Object.assign({}, intervention, {
      dateDebut: intervention.dateDebut != null && intervention.dateDebut.isValid() ? intervention.dateDebut.toJSON() : null,
      dateFin: intervention.dateFin != null && intervention.dateFin.isValid() ? intervention.dateFin.toJSON() : null
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
      res.body.forEach((intervention: IIntervention) => {
        intervention.dateDebut = intervention.dateDebut != null ? moment(intervention.dateDebut) : null;
        intervention.dateFin = intervention.dateFin != null ? moment(intervention.dateFin) : null;
      });
    }
    return res;
  }
}
