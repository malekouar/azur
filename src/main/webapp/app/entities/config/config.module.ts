import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { AzursupportwebtoolSharedModule } from 'app/shared';
import {
  ConfigComponent,
  ConfigDetailComponent,
  ConfigUpdateComponent,
  ConfigDeletePopupComponent,
  ConfigDeleteDialogComponent,
  configRoute,
  configPopupRoute
} from './';

const ENTITY_STATES = [...configRoute, ...configPopupRoute];

@NgModule({
  imports: [AzursupportwebtoolSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [ConfigComponent, ConfigDetailComponent, ConfigUpdateComponent, ConfigDeleteDialogComponent, ConfigDeletePopupComponent],
  entryComponents: [ConfigComponent, ConfigUpdateComponent, ConfigDeleteDialogComponent, ConfigDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolConfigModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
