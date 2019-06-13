import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IServeur } from 'app/shared/model/serveur.model';

type EntityResponseType = HttpResponse<IServeur>;
type EntityArrayResponseType = HttpResponse<IServeur[]>;

@Injectable({ providedIn: 'root' })
export class ServeurService {
  public resourceUrl = SERVER_API_URL + 'api/serveurs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/serveurs';

  constructor(protected http: HttpClient) {}

  create(serveur: IServeur): Observable<EntityResponseType> {
    return this.http.post<IServeur>(this.resourceUrl, serveur, { observe: 'response' });
  }

  update(serveur: IServeur): Observable<EntityResponseType> {
    return this.http.put<IServeur>(this.resourceUrl, serveur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServeur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServeur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServeur[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
