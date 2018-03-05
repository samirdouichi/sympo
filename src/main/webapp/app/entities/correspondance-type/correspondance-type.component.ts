import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CorrespondanceType } from './correspondance-type.model';
import { CorrespondanceTypeService } from './correspondance-type.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-correspondance-type',
    templateUrl: './correspondance-type.component.html'
})
export class CorrespondanceTypeComponent implements OnInit, OnDestroy {
correspondanceTypes: CorrespondanceType[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private correspondanceTypeService: CorrespondanceTypeService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.correspondanceTypeService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CorrespondanceType[]>) => this.correspondanceTypes = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.correspondanceTypeService.query().subscribe(
            (res: HttpResponse<CorrespondanceType[]>) => {
                this.correspondanceTypes = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCorrespondanceTypes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CorrespondanceType) {
        return item.id;
    }
    registerChangeInCorrespondanceTypes() {
        this.eventSubscriber = this.eventManager.subscribe('correspondanceTypeListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
