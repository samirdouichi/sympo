import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Crediteur } from './crediteur.model';
import { CrediteurPopupService } from './crediteur-popup.service';
import { CrediteurService } from './crediteur.service';

@Component({
    selector: 'jhi-crediteur-delete-dialog',
    templateUrl: './crediteur-delete-dialog.component.html'
})
export class CrediteurDeleteDialogComponent {

    crediteur: Crediteur;

    constructor(
        private crediteurService: CrediteurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.crediteurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'crediteurListModification',
                content: 'Deleted an crediteur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-crediteur-delete-popup',
    template: ''
})
export class CrediteurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private crediteurPopupService: CrediteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.crediteurPopupService
                .open(CrediteurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
