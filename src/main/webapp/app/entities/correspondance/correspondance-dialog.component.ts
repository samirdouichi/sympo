import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Correspondance } from './correspondance.model';
import { CorrespondancePopupService } from './correspondance-popup.service';
import { CorrespondanceService } from './correspondance.service';
import { CorrespondanceType, CorrespondanceTypeService } from '../correspondance-type';
import { CorrespondanceDocument, CorrespondanceDocumentService } from '../correspondance-document';
import { Dossier, DossierService } from '../dossier';

@Component({
    selector: 'jhi-correspondance-dialog',
    templateUrl: './correspondance-dialog.component.html'
})
export class CorrespondanceDialogComponent implements OnInit {

    correspondance: Correspondance;
    isSaving: boolean;

    correspondancetypes: CorrespondanceType[];

    correspondancedocuments: CorrespondanceDocument[];

    dossiers: Dossier[];
    createdAtDp: any;
    updatedAtDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private correspondanceService: CorrespondanceService,
        private correspondanceTypeService: CorrespondanceTypeService,
        private correspondanceDocumentService: CorrespondanceDocumentService,
        private dossierService: DossierService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.correspondanceTypeService
            .query({filter: 'custom(codecorrespondance)-is-null'})
            .subscribe((res: HttpResponse<CorrespondanceType[]>) => {
                if (!this.correspondance.correspondanceTypes || !this.correspondance.correspondanceTypes.id) {
                    this.correspondancetypes = res.body;
                } else {
                    this.correspondanceTypeService
                        .find(this.correspondance.correspondanceTypes.id)
                        .subscribe((subRes: HttpResponse<CorrespondanceType>) => {
                            this.correspondancetypes = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.correspondanceDocumentService
            .query({filter: 'custom(correspondancedocument)-is-null'})
            .subscribe((res: HttpResponse<CorrespondanceDocument[]>) => {
                if (!this.correspondance.correspondanceDocuments || !this.correspondance.correspondanceDocuments.id) {
                    this.correspondancedocuments = res.body;
                } else {
                    this.correspondanceDocumentService
                        .find(this.correspondance.correspondanceDocuments.id)
                        .subscribe((subRes: HttpResponse<CorrespondanceDocument>) => {
                            this.correspondancedocuments = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.dossierService.query()
            .subscribe((res: HttpResponse<Dossier[]>) => { this.dossiers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.correspondance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.correspondanceService.update(this.correspondance));
        } else {
            this.subscribeToSaveResponse(
                this.correspondanceService.create(this.correspondance));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Correspondance>>) {
        result.subscribe((res: HttpResponse<Correspondance>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Correspondance) {
        this.eventManager.broadcast({ name: 'correspondanceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCorrespondanceTypeById(index: number, item: CorrespondanceType) {
        return item.id;
    }

    trackCorrespondanceDocumentById(index: number, item: CorrespondanceDocument) {
        return item.id;
    }

    trackDossierById(index: number, item: Dossier) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-correspondance-popup',
    template: ''
})
export class CorrespondancePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private correspondancePopupService: CorrespondancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.correspondancePopupService
                    .open(CorrespondanceDialogComponent as Component, params['id']);
            } else {
                this.correspondancePopupService
                    .open(CorrespondanceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
