import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CabinetComponent } from './cabinet.component';
import { CabinetDetailComponent } from './cabinet-detail.component';
import { CabinetPopupComponent } from './cabinet-dialog.component';
import { CabinetDeletePopupComponent } from './cabinet-delete-dialog.component';

@Injectable()
export class CabinetResolvePagingParams implements Resolve<any> {

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

export const cabinetRoute: Routes = [
    {
        path: 'cabinet',
        component: CabinetComponent,
        resolve: {
            'pagingParams': CabinetResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.cabinet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cabinet/:id',
        component: CabinetDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.cabinet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cabinetPopupRoute: Routes = [
    {
        path: 'cabinet-new',
        component: CabinetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.cabinet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cabinet/:id/edit',
        component: CabinetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.cabinet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cabinet/:id/delete',
        component: CabinetDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'sympoApp.cabinet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
