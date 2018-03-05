import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CreanceService,
    CreancePopupService,
    CreanceComponent,
    CreanceDetailComponent,
    CreanceDialogComponent,
    CreancePopupComponent,
    CreanceDeletePopupComponent,
    CreanceDeleteDialogComponent,
    creanceRoute,
    creancePopupRoute,
} from './';

const ENTITY_STATES = [
    ...creanceRoute,
    ...creancePopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CreanceComponent,
        CreanceDetailComponent,
        CreanceDialogComponent,
        CreanceDeleteDialogComponent,
        CreancePopupComponent,
        CreanceDeletePopupComponent,
    ],
    entryComponents: [
        CreanceComponent,
        CreanceDialogComponent,
        CreancePopupComponent,
        CreanceDeleteDialogComponent,
        CreanceDeletePopupComponent,
    ],
    providers: [
        CreanceService,
        CreancePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCreanceModule {}
