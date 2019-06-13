import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Livraisons } from 'app/shared/model/livraisons.model';
import { LivraisonsService } from './livraisons.service';
import { LivraisonsComponent } from './livraisons.component';
import { LivraisonsDetailComponent } from './livraisons-detail.component';
import { LivraisonsUpdateComponent } from './livraisons-update.component';
import { LivraisonsDeletePopupComponent } from './livraisons-delete-dialog.component';
import { ILivraisons } from 'app/shared/model/livraisons.model';

@Injectable({ providedIn: 'root' })
export class LivraisonsResolve implements Resolve<ILivraisons> {
  constructor(private service: LivraisonsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILivraisons> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Livraisons>) => response.ok),
        map((livraisons: HttpResponse<Livraisons>) => livraisons.body)
      );
    }
    return of(new Livraisons());
  }
}

export const livraisonsRoute: Routes = [
  {
    path: '',
    component: LivraisonsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'azursupportwebtoolApp.livraisons.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LivraisonsDetailComponent,
    resolve: {
      livraisons: LivraisonsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.livraisons.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LivraisonsUpdateComponent,
    resolve: {
      livraisons: LivraisonsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.livraisons.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LivraisonsUpdateComponent,
    resolve: {
      livraisons: LivraisonsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.livraisons.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const livraisonsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LivraisonsDeletePopupComponent,
    resolve: {
      livraisons: LivraisonsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.livraisons.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
