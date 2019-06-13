import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AzursupportwebtoolSharedModule } from 'app/shared';
import {
  LivraisonsComponent,
  LivraisonsDetailComponent,
  LivraisonsUpdateComponent,
  LivraisonsDeletePopupComponent,
  LivraisonsDeleteDialogComponent,
  livraisonsRoute,
  livraisonsPopupRoute
} from './';

const ENTITY_STATES = [...livraisonsRoute, ...livraisonsPopupRoute];

@NgModule({
  imports: [AzursupportwebtoolSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    LivraisonsComponent,
    LivraisonsDetailComponent,
    LivraisonsUpdateComponent,
    LivraisonsDeleteDialogComponent,
    LivraisonsDeletePopupComponent
  ],
  entryComponents: [LivraisonsComponent, LivraisonsUpdateComponent, LivraisonsDeleteDialogComponent, LivraisonsDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolLivraisonsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
