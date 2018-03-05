import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CategorieDossierComponent } from './categorie-dossier.component';
import { CategorieDossierDetailComponent } from './categorie-dossier-detail.component';
import { CategorieDossierPopupComponent } from './categorie-dossier-dialog.component';
import { CategorieDossierDeletePopupComponent } from './categorie-dossier-delete-dialog.component';

export const categorieDossierRoute: Routes = [
    {
        path: 'categorie-dossier',
        component: CategorieDossierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.categorieDossier.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'categorie-dossier/:id',
        component: CategorieDossierDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.categorieDossier.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const categorieDossierPopupRoute: Routes = [
    {
        path: 'categorie-dossier-new',
        component: CategorieDossierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.categorieDossier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categorie-dossier/:id/edit',
        component: CategorieDossierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.categorieDossier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'categorie-dossier/:id/delete',
        component: CategorieDossierDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.categorieDossier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
