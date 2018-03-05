import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Correspondance } from './correspondance.model';
import { CorrespondanceService } from './correspondance.service';

@Injectable()
export class CorrespondancePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private correspondanceService: CorrespondanceService

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
                this.correspondanceService.find(id)
                    .subscribe((correspondanceResponse: HttpResponse<Correspondance>) => {
                        const correspondance: Correspondance = correspondanceResponse.body;
                        if (correspondance.createdAt) {
                            correspondance.createdAt = {
                                year: correspondance.createdAt.getFullYear(),
                                month: correspondance.createdAt.getMonth() + 1,
                                day: correspondance.createdAt.getDate()
                            };
                        }
                        if (correspondance.updatedAt) {
                            correspondance.updatedAt = {
                                year: correspondance.updatedAt.getFullYear(),
                                month: correspondance.updatedAt.getMonth() + 1,
                                day: correspondance.updatedAt.getDate()
                            };
                        }
                        this.ngbModalRef = this.correspondanceModalRef(component, correspondance);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.correspondanceModalRef(component, new Correspondance());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    correspondanceModalRef(component: Component, correspondance: Correspondance): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.correspondance = correspondance;
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
