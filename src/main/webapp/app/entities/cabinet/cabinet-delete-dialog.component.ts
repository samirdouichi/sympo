import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Cabinet } from './cabinet.model';
import { CabinetPopupService } from './cabinet-popup.service';
import { CabinetService } from './cabinet.service';

@Component({
    selector: 'jhi-cabinet-delete-dialog',
    templateUrl: './cabinet-delete-dialog.component.html'
})
export class CabinetDeleteDialogComponent {

    cabinet: Cabinet;

    constructor(
        private cabinetService: CabinetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cabinetService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cabinetListModification',
                content: 'Deleted an cabinet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cabinet-delete-popup',
    template: ''
})
export class CabinetDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cabinetPopupService: CabinetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.cabinetPopupService
                .open(CabinetDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
