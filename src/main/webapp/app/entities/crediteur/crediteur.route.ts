import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CrediteurComponent } from './crediteur.component';
import { CrediteurDetailComponent } from './crediteur-detail.component';
import { CrediteurPopupComponent } from './crediteur-dialog.component';
import { CrediteurDeletePopupComponent } from './crediteur-delete-dialog.component';

@Injectable()
export class CrediteurResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const crediteurRoute: Routes = [
    {
        path: 'crediteur',
        component: CrediteurComponent,
        resolve: {
            'pagingParams': CrediteurResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.crediteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'crediteur/:id',
        component: CrediteurDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.crediteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const crediteurPopupRoute: Routes = [
    {
        path: 'crediteur-new',
        component: CrediteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.crediteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'crediteur/:id/edit',
        component: CrediteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.crediteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'crediteur/:id/delete',
        component: CrediteurDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.crediteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
