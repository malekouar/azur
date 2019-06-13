import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AzursupportwebtoolSharedModule } from 'app/shared';
import {
  ServeurComponent,
  ServeurDetailComponent,
  ServeurUpdateComponent,
  ServeurDeletePopupComponent,
  ServeurDeleteDialogComponent,
  serveurRoute,
  serveurPopupRoute
} from './';

const ENTITY_STATES = [...serveurRoute, ...serveurPopupRoute];

@NgModule({
  imports: [AzursupportwebtoolSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ServeurComponent,
    ServeurDetailComponent,
    ServeurUpdateComponent,
    ServeurDeleteDialogComponent,
    ServeurDeletePopupComponent
  ],
  entryComponents: [ServeurComponent, ServeurUpdateComponent, ServeurDeleteDialogComponent, ServeurDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolServeurModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
