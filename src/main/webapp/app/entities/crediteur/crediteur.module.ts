import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CrediteurService,
    CrediteurPopupService,
    CrediteurComponent,
    CrediteurDetailComponent,
    CrediteurDialogComponent,
    CrediteurPopupComponent,
    CrediteurDeletePopupComponent,
    CrediteurDeleteDialogComponent,
    crediteurRoute,
    crediteurPopupRoute,
    CrediteurResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...crediteurRoute,
    ...crediteurPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CrediteurComponent,
        CrediteurDetailComponent,
        CrediteurDialogComponent,
        CrediteurDeleteDialogComponent,
        CrediteurPopupComponent,
        CrediteurDeletePopupComponent,
    ],
    entryComponents: [
        CrediteurComponent,
        CrediteurDialogComponent,
        CrediteurPopupComponent,
        CrediteurDeleteDialogComponent,
        CrediteurDeletePopupComponent,
    ],
    providers: [
        CrediteurService,
        CrediteurPopupService,
        CrediteurResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCrediteurModule {}
