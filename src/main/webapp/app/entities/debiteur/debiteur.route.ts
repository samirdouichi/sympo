import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { DebiteurComponent } from './debiteur.component';
import { DebiteurDetailComponent } from './debiteur-detail.component';
import { DebiteurPopupComponent } from './debiteur-dialog.component';
import { DebiteurDeletePopupComponent } from './debiteur-delete-dialog.component';

@Injectable()
export class DebiteurResolvePagingParams implements Resolve<any> {

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

export const debiteurRoute: Routes = [
    {
        path: 'debiteur',
        component: DebiteurComponent,
        resolve: {
            'pagingParams': DebiteurResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.debiteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'debiteur/:id',
        component: DebiteurDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.debiteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const debiteurPopupRoute: Routes = [
    {
        path: 'debiteur-new',
        component: DebiteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.debiteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'debiteur/:id/edit',
        component: DebiteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.debiteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'debiteur/:id/delete',
        component: DebiteurDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.debiteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
