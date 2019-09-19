import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { CustomergatewaySharedLibsModule, CustomergatewaySharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [CustomergatewaySharedLibsModule, CustomergatewaySharedCommonModule],
    declarations: [HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    exports: [CustomergatewaySharedCommonModule, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CustomergatewaySharedModule {
    static forRoot() {
        return {
            ngModule: CustomergatewaySharedModule
        };
    }
}
