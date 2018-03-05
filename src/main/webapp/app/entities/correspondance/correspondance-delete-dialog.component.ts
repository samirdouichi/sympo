import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Correspondance } from './correspondance.model';
import { CorrespondancePopupService } from './correspondance-popup.service';
import { CorrespondanceService } from './correspondance.service';

@Component({
    selector: 'jhi-correspondance-delete-dialog',
    templateUrl: './correspondance-delete-dialog.component.html'
})
export class CorrespondanceDeleteDialogComponent {

    correspondance: Correspondance;

    constructor(
        private correspondanceService: CorrespondanceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.correspondanceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'correspondanceListModification',
                content: 'Deleted an correspondance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-correspondance-delete-popup',
    template: ''
})
export class CorrespondanceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private correspondancePopupService: CorrespondancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.correspondancePopupService
                .open(CorrespondanceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
