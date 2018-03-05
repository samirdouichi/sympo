import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CategorieDossierService,
    CategorieDossierPopupService,
    CategorieDossierComponent,
    CategorieDossierDetailComponent,
    CategorieDossierDialogComponent,
    CategorieDossierPopupComponent,
    CategorieDossierDeletePopupComponent,
    CategorieDossierDeleteDialogComponent,
    categorieDossierRoute,
    categorieDossierPopupRoute,
} from './';

const ENTITY_STATES = [
    ...categorieDossierRoute,
    ...categorieDossierPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CategorieDossierComponent,
        CategorieDossierDetailComponent,
        CategorieDossierDialogComponent,
        CategorieDossierDeleteDialogComponent,
        CategorieDossierPopupComponent,
        CategorieDossierDeletePopupComponent,
    ],
    entryComponents: [
        CategorieDossierComponent,
        CategorieDossierDialogComponent,
        CategorieDossierPopupComponent,
        CategorieDossierDeleteDialogComponent,
        CategorieDossierDeletePopupComponent,
    ],
    providers: [
        CategorieDossierService,
        CategorieDossierPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCategorieDossierModule {}
