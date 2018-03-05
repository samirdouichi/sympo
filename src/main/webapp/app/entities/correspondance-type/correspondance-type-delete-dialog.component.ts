import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CorrespondanceType } from './correspondance-type.model';
import { CorrespondanceTypePopupService } from './correspondance-type-popup.service';
import { CorrespondanceTypeService } from './correspondance-type.service';

@Component({
    selector: 'jhi-correspondance-type-delete-dialog',
    templateUrl: './correspondance-type-delete-dialog.component.html'
})
export class CorrespondanceTypeDeleteDialogComponent {

    correspondanceType: CorrespondanceType;

    constructor(
        private correspondanceTypeService: CorrespondanceTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.correspondanceTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'correspondanceTypeListModification',
                content: 'Deleted an correspondanceType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-correspondance-type-delete-popup',
    template: ''
})
export class CorrespondanceTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private correspondanceTypePopupService: CorrespondanceTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.correspondanceTypePopupService
                .open(CorrespondanceTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
