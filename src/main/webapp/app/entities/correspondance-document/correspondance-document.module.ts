import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SympoSharedModule } from '../../shared';
import {
    CorrespondanceDocumentService,
    CorrespondanceDocumentPopupService,
    CorrespondanceDocumentComponent,
    CorrespondanceDocumentDetailComponent,
    CorrespondanceDocumentDialogComponent,
    CorrespondanceDocumentPopupComponent,
    CorrespondanceDocumentDeletePopupComponent,
    CorrespondanceDocumentDeleteDialogComponent,
    correspondanceDocumentRoute,
    correspondanceDocumentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...correspondanceDocumentRoute,
    ...correspondanceDocumentPopupRoute,
];

@NgModule({
    imports: [
        SympoSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CorrespondanceDocumentComponent,
        CorrespondanceDocumentDetailComponent,
        CorrespondanceDocumentDialogComponent,
        CorrespondanceDocumentDeleteDialogComponent,
        CorrespondanceDocumentPopupComponent,
        CorrespondanceDocumentDeletePopupComponent,
    ],
    entryComponents: [
        CorrespondanceDocumentComponent,
        CorrespondanceDocumentDialogComponent,
        CorrespondanceDocumentPopupComponent,
        CorrespondanceDocumentDeleteDialogComponent,
        CorrespondanceDocumentDeletePopupComponent,
    ],
    providers: [
        CorrespondanceDocumentService,
        CorrespondanceDocumentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoCorrespondanceDocumentModule {}
