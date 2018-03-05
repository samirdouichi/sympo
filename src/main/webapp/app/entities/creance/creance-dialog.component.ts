import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Creance } from './creance.model';
import { CreancePopupService } from './creance-popup.service';
import { CreanceService } from './creance.service';
import { Crediteur, CrediteurService } from '../crediteur';

@Component({
    selector: 'jhi-creance-dialog',
    templateUrl: './creance-dialog.component.html'
})
export class CreanceDialogComponent implements OnInit {

    creance: Creance;
    isSaving: boolean;

    crediteurs: Crediteur[];
    createdAtDp: any;
    updatedAtDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private creanceService: CreanceService,
        private crediteurService: CrediteurService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.crediteurService.query()
            .subscribe((res: HttpResponse<Crediteur[]>) => { this.crediteurs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.creance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.creanceService.update(this.creance));
        } else {
            this.subscribeToSaveResponse(
                this.creanceService.create(this.creance));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Creance>>) {
        result.subscribe((res: HttpResponse<Creance>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Creance) {
        this.eventManager.broadcast({ name: 'creanceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCrediteurById(index: number, item: Crediteur) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-creance-popup',
    template: ''
})
export class CreancePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private creancePopupService: CreancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.creancePopupService
                    .open(CreanceDialogComponent as Component, params['id']);
            } else {
                this.creancePopupService
                    .open(CreanceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
