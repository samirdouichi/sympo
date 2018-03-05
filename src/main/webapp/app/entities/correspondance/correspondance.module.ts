import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CorrespondanceService,
    CorrespondancePopupService,
    CorrespondanceComponent,
    CorrespondanceDetailComponent,
    CorrespondanceDialogComponent,
    CorrespondancePopupComponent,
    CorrespondanceDeletePopupComponent,
    CorrespondanceDeleteDialogComponent,
    correspondanceRoute,
    correspondancePopupRoute,
    CorrespondanceResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...correspondanceRoute,
    ...correspondancePopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CorrespondanceComponent,
        CorrespondanceDetailComponent,
        CorrespondanceDialogComponent,
        CorrespondanceDeleteDialogComponent,
        CorrespondancePopupComponent,
        CorrespondanceDeletePopupComponent,
    ],
    entryComponents: [
        CorrespondanceComponent,
        CorrespondanceDialogComponent,
        CorrespondancePopupComponent,
        CorrespondanceDeleteDialogComponent,
        CorrespondanceDeletePopupComponent,
    ],
    providers: [
        CorrespondanceService,
        CorrespondancePopupService,
        CorrespondanceResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCorrespondanceModule {}
