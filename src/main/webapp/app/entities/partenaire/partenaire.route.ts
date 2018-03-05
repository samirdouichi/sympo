import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PartenaireComponent } from './partenaire.component';
import { PartenaireDetailComponent } from './partenaire-detail.component';
import { PartenairePopupComponent } from './partenaire-dialog.component';
import { PartenaireDeletePopupComponent } from './partenaire-delete-dialog.component';

export const partenaireRoute: Routes = [
    {
        path: 'partenaire',
        component: PartenaireComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'partenaire/:id',
        component: PartenaireDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const partenairePopupRoute: Routes = [
    {
        path: 'partenaire-new',
        component: PartenairePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'partenaire/:id/edit',
        component: PartenairePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'partenaire/:id/delete',
        component: PartenaireDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.partenaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
