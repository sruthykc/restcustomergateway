import { NgModule } from '@angular/core';

import { CustomergatewaySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [CustomergatewaySharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [CustomergatewaySharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class CustomergatewaySharedCommonModule {}
