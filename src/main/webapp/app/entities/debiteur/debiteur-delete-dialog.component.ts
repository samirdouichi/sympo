import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Debiteur } from './debiteur.model';
import { DebiteurPopupService } from './debiteur-popup.service';
import { DebiteurService } from './debiteur.service';

@Component({
    selector: 'jhi-debiteur-delete-dialog',
    templateUrl: './debiteur-delete-dialog.component.html'
})
export class DebiteurDeleteDialogComponent {

    debiteur: Debiteur;

    constructor(
        private debiteurService: DebiteurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.debiteurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'debiteurListModification',
                content: 'Deleted an debiteur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-debiteur-delete-popup',
    template: ''
})
export class DebiteurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private debiteurPopupService: DebiteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.debiteurPopupService
                .open(DebiteurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
