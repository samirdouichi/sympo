import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CategorieDossier } from './categorie-dossier.model';
import { CategorieDossierPopupService } from './categorie-dossier-popup.service';
import { CategorieDossierService } from './categorie-dossier.service';

@Component({
    selector: 'jhi-categorie-dossier-delete-dialog',
    templateUrl: './categorie-dossier-delete-dialog.component.html'
})
export class CategorieDossierDeleteDialogComponent {

    categorieDossier: CategorieDossier;

    constructor(
        private categorieDossierService: CategorieDossierService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.categorieDossierService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'categorieDossierListModification',
                content: 'Deleted an categorieDossier'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-categorie-dossier-delete-popup',
    template: ''
})
export class CategorieDossierDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private categorieDossierPopupService: CategorieDossierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.categorieDossierPopupService
                .open(CategorieDossierDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
