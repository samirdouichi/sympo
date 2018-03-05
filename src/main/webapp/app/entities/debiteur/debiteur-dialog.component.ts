import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Debiteur } from './debiteur.model';
import { DebiteurPopupService } from './debiteur-popup.service';
import { DebiteurService } from './debiteur.service';
import { Dossier, DossierService } from '../dossier';

@Component({
    selector: 'jhi-debiteur-dialog',
    templateUrl: './debiteur-dialog.component.html'
})
export class DebiteurDialogComponent implements OnInit {

    debiteur: Debiteur;
    isSaving: boolean;

    dossiers: Dossier[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private debiteurService: DebiteurService,
        private dossierService: DossierService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.dossierService.query()
            .subscribe((res: HttpResponse<Dossier[]>) => { this.dossiers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.debiteur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.debiteurService.update(this.debiteur));
        } else {
            this.subscribeToSaveResponse(
                this.debiteurService.create(this.debiteur));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Debiteur>>) {
        result.subscribe((res: HttpResponse<Debiteur>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Debiteur) {
        this.eventManager.broadcast({ name: 'debiteurListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDossierById(index: number, item: Dossier) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-debiteur-popup',
    template: ''
})
export class DebiteurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private debiteurPopupService: DebiteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.debiteurPopupService
                    .open(DebiteurDialogComponent as Component, params['id']);
            } else {
                this.debiteurPopupService
                    .open(DebiteurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
