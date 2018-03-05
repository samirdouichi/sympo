import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Partenaire } from './partenaire.model';
import { PartenairePopupService } from './partenaire-popup.service';
import { PartenaireService } from './partenaire.service';

@Component({
    selector: 'jhi-partenaire-delete-dialog',
    templateUrl: './partenaire-delete-dialog.component.html'
})
export class PartenaireDeleteDialogComponent {

    partenaire: Partenaire;

    constructor(
        private partenaireService: PartenaireService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.partenaireService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'partenaireListModification',
                content: 'Deleted an partenaire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-partenaire-delete-popup',
    template: ''
})
export class PartenaireDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private partenairePopupService: PartenairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.partenairePopupService
                .open(PartenaireDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
