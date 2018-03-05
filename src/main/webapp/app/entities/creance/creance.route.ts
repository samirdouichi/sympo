import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CreanceComponent } from './creance.component';
import { CreanceDetailComponent } from './creance-detail.component';
import { CreancePopupComponent } from './creance-dialog.component';
import { CreanceDeletePopupComponent } from './creance-delete-dialog.component';

export const creanceRoute: Routes = [
    {
        path: 'creance',
        component: CreanceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.creance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'creance/:id',
        component: CreanceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.creance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const creancePopupRoute: Routes = [
    {
        path: 'creance-new',
        component: CreancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.creance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'creance/:id/edit',
        component: CreancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.creance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'creance/:id/delete',
        component: CreanceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.creance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
