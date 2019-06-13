import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AzursupportwebtoolSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [AzursupportwebtoolSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [AzursupportwebtoolSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolSharedModule {
  static forRoot() {
    return {
      ngModule: AzursupportwebtoolSharedModule
    };
  }
}
