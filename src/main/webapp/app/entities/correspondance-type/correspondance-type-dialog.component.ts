import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CorrespondanceType } from './correspondance-type.model';
import { CorrespondanceTypePopupService } from './correspondance-type-popup.service';
import { CorrespondanceTypeService } from './correspondance-type.service';
import { Correspondance, CorrespondanceService } from '../correspondance';

@Component({
    selector: 'jhi-correspondance-type-dialog',
    templateUrl: './correspondance-type-dialog.component.html'
})
export class CorrespondanceTypeDialogComponent implements OnInit {

    correspondanceType: CorrespondanceType;
    isSaving: boolean;

    correspondances: Correspondance[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private correspondanceTypeService: CorrespondanceTypeService,
        private correspondanceService: CorrespondanceService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.correspondanceService.query()
            .subscribe((res: HttpResponse<Correspondance[]>) => { this.correspondances = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.correspondanceType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.correspondanceTypeService.update(this.correspondanceType));
        } else {
            this.subscribeToSaveResponse(
                this.correspondanceTypeService.create(this.correspondanceType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CorrespondanceType>>) {
        result.subscribe((res: HttpResponse<CorrespondanceType>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CorrespondanceType) {
        this.eventManager.broadcast({ name: 'correspondanceTypeListModification', content: 'OK'});
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
    selector: 'jhi-correspondance-type-popup',
    template: ''
})
export class CorrespondanceTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private correspondanceTypePopupService: CorrespondanceTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.correspondanceTypePopupService
                    .open(CorrespondanceTypeDialogComponent as Component, params['id']);
            } else {
                this.correspondanceTypePopupService
                    .open(CorrespondanceTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
