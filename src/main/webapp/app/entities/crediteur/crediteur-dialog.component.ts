import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Crediteur } from './crediteur.model';
import { CrediteurPopupService } from './crediteur-popup.service';
import { CrediteurService } from './crediteur.service';
import { DossierInstruction, DossierInstructionService } from '../dossier-instruction';

@Component({
    selector: 'jhi-crediteur-dialog',
    templateUrl: './crediteur-dialog.component.html'
})
export class CrediteurDialogComponent implements OnInit {

    crediteur: Crediteur;
    isSaving: boolean;

    crediteurs: DossierInstruction[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private crediteurService: CrediteurService,
        private dossierInstructionService: DossierInstructionService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.dossierInstructionService
            .query({filter: 'custom(numinstruction)-is-null'})
            .subscribe((res: HttpResponse<DossierInstruction[]>) => {
                if (!this.crediteur.crediteur || !this.crediteur.crediteur.id) {
                    this.crediteurs = res.body;
                } else {
                    this.dossierInstructionService
                        .find(this.crediteur.crediteur.id)
                        .subscribe((subRes: HttpResponse<DossierInstruction>) => {
                            this.crediteurs = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.crediteur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.crediteurService.update(this.crediteur));
        } else {
            this.subscribeToSaveResponse(
                this.crediteurService.create(this.crediteur));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Crediteur>>) {
        result.subscribe((res: HttpResponse<Crediteur>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Crediteur) {
        this.eventManager.broadcast({ name: 'crediteurListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDossierInstructionById(index: number, item: DossierInstruction) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-crediteur-popup',
    template: ''
})
export class CrediteurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private crediteurPopupService: CrediteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.crediteurPopupService
                    .open(CrediteurDialogComponent as Component, params['id']);
            } else {
                this.crediteurPopupService
                    .open(CrediteurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
