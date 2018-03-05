import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { CorrespondanceDocument } from './correspondance-document.model';
import { CorrespondanceDocumentService } from './correspondance-document.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-correspondance-document',
    templateUrl: './correspondance-document.component.html'
})
export class CorrespondanceDocumentComponent implements OnInit, OnDestroy {
correspondanceDocuments: CorrespondanceDocument[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private correspondanceDocumentService: CorrespondanceDocumentService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.correspondanceDocumentService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CorrespondanceDocument[]>) => this.correspondanceDocuments = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.correspondanceDocumentService.query().subscribe(
            (res: HttpResponse<CorrespondanceDocument[]>) => {
                this.correspondanceDocuments = res.body;
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
        this.registerChangeInCorrespondanceDocuments();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CorrespondanceDocument) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInCorrespondanceDocuments() {
        this.eventSubscriber = this.eventManager.subscribe('correspondanceDocumentListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
