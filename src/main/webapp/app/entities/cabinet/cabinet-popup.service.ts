import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Cabinet } from './cabinet.model';
import { CabinetService } from './cabinet.service';

@Injectable()
export class CabinetPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cabinetService: CabinetService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.cabinetService.find(id)
                    .subscribe((cabinetResponse: HttpResponse<Cabinet>) => {
                        const cabinet: Cabinet = cabinetResponse.body;
                        if (cabinet.dateCreation) {
                            cabinet.dateCreation = {
                                year: cabinet.dateCreation.getFullYear(),
                                month: cabinet.dateCreation.getMonth() + 1,
                                day: cabinet.dateCreation.getDate()
                            };
                        }
                        this.ngbModalRef = this.cabinetModalRef(component, cabinet);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.cabinetModalRef(component, new Cabinet());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    cabinetModalRef(component: Component, cabinet: Cabinet): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cabinet = cabinet;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
