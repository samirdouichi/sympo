import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CorrespondanceTypeComponent } from './correspondance-type.component';
import { CorrespondanceTypeDetailComponent } from './correspondance-type-detail.component';
import { CorrespondanceTypePopupComponent } from './correspondance-type-dialog.component';
import { CorrespondanceTypeDeletePopupComponent } from './correspondance-type-delete-dialog.component';

export const correspondanceTypeRoute: Routes = [
    {
        path: 'correspondance-type',
        component: CorrespondanceTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'correspondance-type/:id',
        component: CorrespondanceTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const correspondanceTypePopupRoute: Routes = [
    {
        path: 'correspondance-type-new',
        component: CorrespondanceTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'correspondance-type/:id/edit',
        component: CorrespondanceTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'correspondance-type/:id/delete',
        component: CorrespondanceTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondanceType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
