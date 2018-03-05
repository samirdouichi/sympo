import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    DossierInstructionService,
    DossierInstructionPopupService,
    DossierInstructionComponent,
    DossierInstructionDetailComponent,
    DossierInstructionDialogComponent,
    DossierInstructionPopupComponent,
    DossierInstructionDeletePopupComponent,
    DossierInstructionDeleteDialogComponent,
    dossierInstructionRoute,
    dossierInstructionPopupRoute,
} from './';

const ENTITY_STATES = [
    ...dossierInstructionRoute,
    ...dossierInstructionPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DossierInstructionComponent,
        DossierInstructionDetailComponent,
        DossierInstructionDialogComponent,
        DossierInstructionDeleteDialogComponent,
        DossierInstructionPopupComponent,
        DossierInstructionDeletePopupComponent,
    ],
    entryComponents: [
        DossierInstructionComponent,
        DossierInstructionDialogComponent,
        DossierInstructionPopupComponent,
        DossierInstructionDeleteDialogComponent,
        DossierInstructionDeletePopupComponent,
    ],
    providers: [
        DossierInstructionService,
        DossierInstructionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoDossierInstructionModule {}
