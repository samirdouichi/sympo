import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { DossierInstruction } from './dossier-instruction.model';
import { DossierInstructionPopupService } from './dossier-instruction-popup.service';
import { DossierInstructionService } from './dossier-instruction.service';
import { Crediteur, CrediteurService } from '../crediteur';
import { Dossier, DossierService } from '../dossier';

@Component({
    selector: 'jhi-dossier-instruction-dialog',
    templateUrl: './dossier-instruction-dialog.component.html'
})
export class DossierInstructionDialogComponent implements OnInit {

    dossierInstruction: DossierInstruction;
    isSaving: boolean;

    crediteurs: Crediteur[];

    dossiers: Dossier[];
    dateCreationInstructionDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private dossierInstructionService: DossierInstructionService,
        private crediteurService: CrediteurService,
        private dossierService: DossierService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.crediteurService.query()
            .subscribe((res: HttpResponse<Crediteur[]>) => { this.crediteurs = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.dossierService.query()
            .subscribe((res: HttpResponse<Dossier[]>) => { this.dossiers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.dossierInstruction.id !== undefined) {
            this.subscribeToSaveResponse(
                this.dossierInstructionService.update(this.dossierInstruction));
        } else {
            this.subscribeToSaveResponse(
                this.dossierInstructionService.create(this.dossierInstruction));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<DossierInstruction>>) {
        result.subscribe((res: HttpResponse<DossierInstruction>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: DossierInstruction) {
        this.eventManager.broadcast({ name: 'dossierInstructionListModification', content: 'OK'});
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

    trackDossierById(index: number, item: Dossier) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-dossier-instruction-popup',
    template: ''
})
export class DossierInstructionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dossierInstructionPopupService: DossierInstructionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.dossierInstructionPopupService
                    .open(DossierInstructionDialogComponent as Component, params['id']);
            } else {
                this.dossierInstructionPopupService
                    .open(DossierInstructionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
