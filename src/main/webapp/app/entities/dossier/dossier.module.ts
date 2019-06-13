import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AzursupportwebtoolSharedModule } from 'app/shared';
import {
  DossierComponent,
  DossierDetailComponent,
  DossierUpdateComponent,
  DossierDeletePopupComponent,
  DossierDeleteDialogComponent,
  dossierRoute,
  dossierPopupRoute
} from './';

const ENTITY_STATES = [...dossierRoute, ...dossierPopupRoute];

@NgModule({
  imports: [AzursupportwebtoolSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DossierComponent,
    DossierDetailComponent,
    DossierUpdateComponent,
    DossierDeleteDialogComponent,
    DossierDeletePopupComponent
  ],
  entryComponents: [DossierComponent, DossierUpdateComponent, DossierDeleteDialogComponent, DossierDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolDossierModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
