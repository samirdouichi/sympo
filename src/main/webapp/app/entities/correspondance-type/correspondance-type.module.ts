import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CorrespondanceTypeService,
    CorrespondanceTypePopupService,
    CorrespondanceTypeComponent,
    CorrespondanceTypeDetailComponent,
    CorrespondanceTypeDialogComponent,
    CorrespondanceTypePopupComponent,
    CorrespondanceTypeDeletePopupComponent,
    CorrespondanceTypeDeleteDialogComponent,
    correspondanceTypeRoute,
    correspondanceTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...correspondanceTypeRoute,
    ...correspondanceTypePopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CorrespondanceTypeComponent,
        CorrespondanceTypeDetailComponent,
        CorrespondanceTypeDialogComponent,
        CorrespondanceTypeDeleteDialogComponent,
        CorrespondanceTypePopupComponent,
        CorrespondanceTypeDeletePopupComponent,
    ],
    entryComponents: [
        CorrespondanceTypeComponent,
        CorrespondanceTypeDialogComponent,
        CorrespondanceTypePopupComponent,
        CorrespondanceTypeDeleteDialogComponent,
        CorrespondanceTypeDeletePopupComponent,
    ],
    providers: [
        CorrespondanceTypeService,
        CorrespondanceTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCorrespondanceTypeModule {}
