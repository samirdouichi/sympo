import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    DossierService,
    DossierPopupService,
    DossierComponent,
    DossierDetailComponent,
    DossierDialogComponent,
    DossierPopupComponent,
    DossierDeletePopupComponent,
    DossierDeleteDialogComponent,
    dossierRoute,
    dossierPopupRoute,
    DossierResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...dossierRoute,
    ...dossierPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DossierComponent,
        DossierDetailComponent,
        DossierDialogComponent,
        DossierDeleteDialogComponent,
        DossierPopupComponent,
        DossierDeletePopupComponent,
    ],
    entryComponents: [
        DossierComponent,
        DossierDialogComponent,
        DossierPopupComponent,
        DossierDeleteDialogComponent,
        DossierDeletePopupComponent,
    ],
    providers: [
        DossierService,
        DossierPopupService,
        DossierResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoDossierModule {}
