import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CategorieDossier } from './categorie-dossier.model';
import { CategorieDossierPopupService } from './categorie-dossier-popup.service';
import { CategorieDossierService } from './categorie-dossier.service';
import { Dossier, DossierService } from '../dossier';

@Component({
    selector: 'jhi-categorie-dossier-dialog',
    templateUrl: './categorie-dossier-dialog.component.html'
})
export class CategorieDossierDialogComponent implements OnInit {

    categorieDossier: CategorieDossier;
    isSaving: boolean;

    dossiers: Dossier[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private categorieDossierService: CategorieDossierService,
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
        if (this.categorieDossier.id !== undefined) {
            this.subscribeToSaveResponse(
                this.categorieDossierService.update(this.categorieDossier));
        } else {
            this.subscribeToSaveResponse(
                this.categorieDossierService.create(this.categorieDossier));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CategorieDossier>>) {
        result.subscribe((res: HttpResponse<CategorieDossier>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CategorieDossier) {
        this.eventManager.broadcast({ name: 'categorieDossierListModification', content: 'OK'});
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
    selector: 'jhi-categorie-dossier-popup',
    template: ''
})
export class CategorieDossierPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private categorieDossierPopupService: CategorieDossierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.categorieDossierPopupService
                    .open(CategorieDossierDialogComponent as Component, params['id']);
            } else {
                this.categorieDossierPopupService
                    .open(CategorieDossierDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
