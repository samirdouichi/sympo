import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    PartenaireService,
    PartenairePopupService,
    PartenaireComponent,
    PartenaireDetailComponent,
    PartenaireDialogComponent,
    PartenairePopupComponent,
    PartenaireDeletePopupComponent,
    PartenaireDeleteDialogComponent,
    partenaireRoute,
    partenairePopupRoute,
} from './';

const ENTITY_STATES = [
    ...partenaireRoute,
    ...partenairePopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PartenaireComponent,
        PartenaireDetailComponent,
        PartenaireDialogComponent,
        PartenaireDeleteDialogComponent,
        PartenairePopupComponent,
        PartenaireDeletePopupComponent,
    ],
    entryComponents: [
        PartenaireComponent,
        PartenaireDialogComponent,
        PartenairePopupComponent,
        PartenaireDeleteDialogComponent,
        PartenaireDeletePopupComponent,
    ],
    providers: [
        PartenaireService,
        PartenairePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoPartenaireModule {}
