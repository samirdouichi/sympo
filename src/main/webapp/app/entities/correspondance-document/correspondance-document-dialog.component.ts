import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { CorrespondanceDocument } from './correspondance-document.model';
import { CorrespondanceDocumentPopupService } from './correspondance-document-popup.service';
import { CorrespondanceDocumentService } from './correspondance-document.service';
import { Correspondance, CorrespondanceService } from '../correspondance';

@Component({
    selector: 'jhi-correspondance-document-dialog',
    templateUrl: './correspondance-document-dialog.component.html'
})
export class CorrespondanceDocumentDialogComponent implements OnInit {

    correspondanceDocument: CorrespondanceDocument;
    isSaving: boolean;

    correspondances: Correspondance[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private correspondanceDocumentService: CorrespondanceDocumentService,
        private correspondanceService: CorrespondanceService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.correspondanceService.query()
            .subscribe((res: HttpResponse<Correspondance[]>) => { this.correspondances = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.correspondanceDocument.id !== undefined) {
            this.subscribeToSaveResponse(
                this.correspondanceDocumentService.update(this.correspondanceDocument));
        } else {
            this.subscribeToSaveResponse(
                this.correspondanceDocumentService.create(this.correspondanceDocument));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CorrespondanceDocument>>) {
        result.subscribe((res: HttpResponse<CorrespondanceDocument>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CorrespondanceDocument) {
        this.eventManager.broadcast({ name: 'correspondanceDocumentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCorrespondanceById(index: number, item: Correspondance) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-correspondance-document-popup',
    template: ''
})
export class CorrespondanceDocumentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private correspondanceDocumentPopupService: CorrespondanceDocumentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.correspondanceDocumentPopupService
                    .open(CorrespondanceDocumentDialogComponent as Component, params['id']);
            } else {
                this.correspondanceDocumentPopupService
                    .open(CorrespondanceDocumentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
