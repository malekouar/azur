import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Intervention } from 'app/shared/model/intervention.model';
import { InterventionService } from './intervention.service';
import { InterventionComponent } from './intervention.component';
import { InterventionDetailComponent } from './intervention-detail.component';
import { InterventionUpdateComponent } from './intervention-update.component';
import { InterventionDeletePopupComponent } from './intervention-delete-dialog.component';
import { IIntervention } from 'app/shared/model/intervention.model';

@Injectable({ providedIn: 'root' })
export class InterventionResolve implements Resolve<IIntervention> {
  constructor(private service: InterventionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIntervention> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Intervention>) => response.ok),
        map((intervention: HttpResponse<Intervention>) => intervention.body)
      );
    }
    return of(new Intervention());
  }
}

export const interventionRoute: Routes = [
  {
    path: '',
    component: InterventionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'azursupportwebtoolApp.intervention.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InterventionDetailComponent,
    resolve: {
      intervention: InterventionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.intervention.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InterventionUpdateComponent,
    resolve: {
      intervention: InterventionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.intervention.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InterventionUpdateComponent,
    resolve: {
      intervention: InterventionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.intervention.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const interventionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: InterventionDeletePopupComponent,
    resolve: {
      intervention: InterventionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'azursupportwebtoolApp.intervention.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
