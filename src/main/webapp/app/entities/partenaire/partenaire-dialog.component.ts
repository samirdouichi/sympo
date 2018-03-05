import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Partenaire } from './partenaire.model';
import { PartenairePopupService } from './partenaire-popup.service';
import { PartenaireService } from './partenaire.service';
import { Dossier, DossierService } from '../dossier';

@Component({
    selector: 'jhi-partenaire-dialog',
    templateUrl: './partenaire-dialog.component.html'
})
export class PartenaireDialogComponent implements OnInit {

    partenaire: Partenaire;
    isSaving: boolean;

    dossiers: Dossier[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private partenaireService: PartenaireService,
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
        if (this.partenaire.id !== undefined) {
            this.subscribeToSaveResponse(
                this.partenaireService.update(this.partenaire));
        } else {
            this.subscribeToSaveResponse(
                this.partenaireService.create(this.partenaire));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Partenaire>>) {
        result.subscribe((res: HttpResponse<Partenaire>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Partenaire) {
        this.eventManager.broadcast({ name: 'partenaireListModification', content: 'OK'});
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
    selector: 'jhi-partenaire-popup',
    template: ''
})
export class PartenairePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private partenairePopupService: PartenairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.partenairePopupService
                    .open(PartenaireDialogComponent as Component, params['id']);
            } else {
                this.partenairePopupService
                    .open(PartenaireDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
