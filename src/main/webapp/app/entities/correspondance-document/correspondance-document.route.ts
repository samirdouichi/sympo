import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CorrespondanceDocumentComponent } from './correspondance-document.component';
import { CorrespondanceDocumentDetailComponent } from './correspondance-document-detail.component';
import { CorrespondanceDocumentPopupComponent } from './correspondance-document-dialog.component';
import { CorrespondanceDocumentDeletePopupComponent } from './correspondance-document-delete-dialog.component';

export const correspondanceDocumentRoute: Routes = [
    {
        path: 'correspondance-document',
        component: CorrespondanceDocumentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceDocument.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'correspondance-document/:id',
        component: CorrespondanceDocumentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceDocument.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const correspondanceDocumentPopupRoute: Routes = [
    {
        path: 'correspondance-document-new',
        component: CorrespondanceDocumentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceDocument.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'correspondance-document/:id/edit',
        component: CorrespondanceDocumentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceDocument.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'correspondance-document/:id/delete',
        component: CorrespondanceDocumentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceDocument.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
