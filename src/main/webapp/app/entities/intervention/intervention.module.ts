import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AzursupportwebtoolSharedModule } from 'app/shared';
import {
  InterventionComponent,
  InterventionDetailComponent,
  InterventionUpdateComponent,
  InterventionDeletePopupComponent,
  InterventionDeleteDialogComponent,
  interventionRoute,
  interventionPopupRoute
} from './';

const ENTITY_STATES = [...interventionRoute, ...interventionPopupRoute];

@NgModule({
  imports: [AzursupportwebtoolSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    InterventionComponent,
    InterventionDetailComponent,
    InterventionUpdateComponent,
    InterventionDeleteDialogComponent,
    InterventionDeletePopupComponent
  ],
  entryComponents: [
    InterventionComponent,
    InterventionUpdateComponent,
    InterventionDeleteDialogComponent,
    InterventionDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolInterventionModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
