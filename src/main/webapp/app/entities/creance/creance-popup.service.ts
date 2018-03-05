import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Creance } from './creance.model';
import { CreanceService } from './creance.service';

@Injectable()
export class CreancePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private creanceService: CreanceService

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
                this.creanceService.find(id)
                    .subscribe((creanceResponse: HttpResponse<Creance>) => {
                        const creance: Creance = creanceResponse.body;
                        if (creance.createdAt) {
                            creance.createdAt = {
                                year: creance.createdAt.getFullYear(),
                                month: creance.createdAt.getMonth() + 1,
                                day: creance.createdAt.getDate()
                            };
                        }
                        if (creance.updatedAt) {
                            creance.updatedAt = {
                                year: creance.updatedAt.getFullYear(),
                                month: creance.updatedAt.getMonth() + 1,
                                day: creance.updatedAt.getDate()
                            };
                        }
                        this.ngbModalRef = this.creanceModalRef(component, creance);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.creanceModalRef(component, new Creance());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    creanceModalRef(component: Component, creance: Creance): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.creance = creance;
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
