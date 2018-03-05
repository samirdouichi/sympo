import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cabinet } from './cabinet.model';
import { CabinetPopupService } from './cabinet-popup.service';
import { CabinetService } from './cabinet.service';

@Component({
    selector: 'jhi-cabinet-dialog',
    templateUrl: './cabinet-dialog.component.html'
})
export class CabinetDialogComponent implements OnInit {

    cabinet: Cabinet;
    isSaving: boolean;
    dateCreationDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private cabinetService: CabinetService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cabinet.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cabinetService.update(this.cabinet));
        } else {
            this.subscribeToSaveResponse(
                this.cabinetService.create(this.cabinet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Cabinet>>) {
        result.subscribe((res: HttpResponse<Cabinet>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Cabinet) {
        this.eventManager.broadcast({ name: 'cabinetListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-cabinet-popup',
    template: ''
})
export class CabinetPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cabinetPopupService: CabinetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.cabinetPopupService
                    .open(CabinetDialogComponent as Component, params['id']);
            } else {
                this.cabinetPopupService
                    .open(CabinetDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
