import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DossierInstructionComponent } from './dossier-instruction.component';
import { DossierInstructionDetailComponent } from './dossier-instruction-detail.component';
import { DossierInstructionPopupComponent } from './dossier-instruction-dialog.component';
import { DossierInstructionDeletePopupComponent } from './dossier-instruction-delete-dialog.component';

export const dossierInstructionRoute: Routes = [
    {
        path: 'dossier-instruction',
        component: DossierInstructionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.dossierInstruction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'dossier-instruction/:id',
        component: DossierInstructionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.dossierInstruction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dossierInstructionPopupRoute: Routes = [
    {
        path: 'dossier-instruction-new',
        component: DossierInstructionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.dossierInstruction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dossier-instruction/:id/edit',
        component: DossierInstructionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.dossierInstruction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dossier-instruction/:id/delete',
        component: DossierInstructionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.dossierInstruction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
