import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    DebiteurService,
    DebiteurPopupService,
    DebiteurComponent,
    DebiteurDetailComponent,
    DebiteurDialogComponent,
    DebiteurPopupComponent,
    DebiteurDeletePopupComponent,
    DebiteurDeleteDialogComponent,
    debiteurRoute,
    debiteurPopupRoute,
    DebiteurResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...debiteurRoute,
    ...debiteurPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DebiteurComponent,
        DebiteurDetailComponent,
        DebiteurDialogComponent,
        DebiteurDeleteDialogComponent,
        DebiteurPopupComponent,
        DebiteurDeletePopupComponent,
    ],
    entryComponents: [
        DebiteurComponent,
        DebiteurDialogComponent,
        DebiteurPopupComponent,
        DebiteurDeleteDialogComponent,
        DebiteurDeletePopupComponent,
    ],
    providers: [
        DebiteurService,
        DebiteurPopupService,
        DebiteurResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoDebiteurModule {}
