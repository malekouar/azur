import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILivraisons } from 'app/shared/model/livraisons.model';

type EntityResponseType = HttpResponse<ILivraisons>;
type EntityArrayResponseType = HttpResponse<ILivraisons[]>;

@Injectable({ providedIn: 'root' })
export class LivraisonsService {
  public resourceUrl = SERVER_API_URL + 'api/livraisons';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/livraisons';

  constructor(protected http: HttpClient) {}

  create(livraisons: ILivraisons): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraisons);
    return this.http
      .post<ILivraisons>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(livraisons: ILivraisons): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(livraisons);
    return this.http
      .put<ILivraisons>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILivraisons>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILivraisons[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILivraisons[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(livraisons: ILivraisons): ILivraisons {
    const copy: ILivraisons = Object.assign({}, livraisons, {
      dateLivraison: livraisons.dateLivraison != null && livraisons.dateLivraison.isValid() ? livraisons.dateLivraison.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateLivraison = res.body.dateLivraison != null ? moment(res.body.dateLivraison) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((livraisons: ILivraisons) => {
        livraisons.dateLivraison = livraisons.dateLivraison != null ? moment(livraisons.dateLivraison) : null;
      });
    }
    return res;
  }
}
