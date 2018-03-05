import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CorrespondanceComponent } from './correspondance.component';
import { CorrespondanceDetailComponent } from './correspondance-detail.component';
import { CorrespondancePopupComponent } from './correspondance-dialog.component';
import { CorrespondanceDeletePopupComponent } from './correspondance-delete-dialog.component';

@Injectable()
export class CorrespondanceResolvePagingParams implements Resolve<any> {

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

export const correspondanceRoute: Routes = [
    {
        path: 'correspondance',
        component: CorrespondanceComponent,
        resolve: {
            'pagingParams': CorrespondanceResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'correspondance/:id',
        component: CorrespondanceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const correspondancePopupRoute: Routes = [
    {
        path: 'correspondance-new',
        component: CorrespondancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'correspondance/:id/edit',
        component: CorrespondancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'correspondance/:id/delete',
        component: CorrespondanceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.correspondance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
