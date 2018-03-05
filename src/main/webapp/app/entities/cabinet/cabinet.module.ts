import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CabinetService,
    CabinetPopupService,
    CabinetComponent,
    CabinetDetailComponent,
    CabinetDialogComponent,
    CabinetPopupComponent,
    CabinetDeletePopupComponent,
    CabinetDeleteDialogComponent,
    cabinetRoute,
    cabinetPopupRoute,
    CabinetResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cabinetRoute,
    ...cabinetPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CabinetComponent,
        CabinetDetailComponent,
        CabinetDialogComponent,
        CabinetDeleteDialogComponent,
        CabinetPopupComponent,
        CabinetDeletePopupComponent,
    ],
    entryComponents: [
        CabinetComponent,
        CabinetDialogComponent,
        CabinetPopupComponent,
        CabinetDeleteDialogComponent,
        CabinetDeletePopupComponent,
    ],
    providers: [
        CabinetService,
        CabinetPopupService,
        CabinetResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCabinetModule {}
