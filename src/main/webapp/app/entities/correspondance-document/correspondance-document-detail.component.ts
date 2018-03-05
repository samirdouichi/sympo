import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { CorrespondanceDocument } from './correspondance-document.model';
import { CorrespondanceDocumentService } from './correspondance-document.service';

@Component({
    selector: 'jhi-correspondance-document-detail',
    templateUrl: './correspondance-document-detail.component.html'
})
export class CorrespondanceDocumentDetailComponent implements OnInit, OnDestroy {

    correspondanceDocument: CorrespondanceDocument;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private correspondanceDocumentService: CorrespondanceDocumentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCorrespondanceDocuments();
    }

    load(id) {
        this.correspondanceDocumentService.find(id)
            .subscribe((correspondanceDocumentResponse: HttpResponse<CorrespondanceDocument>) => {
                this.correspondanceDocument = correspondanceDocumentResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCorrespondanceDocuments() {
        this.eventSubscriber = this.eventManager.subscribe(
            'correspondanceDocumentListModification',
            (response) => this.load(this.correspondanceDocument.id)
        );
    }
}
