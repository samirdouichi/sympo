import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Dossier } from './dossier.model';
import { DossierService } from './dossier.service';

@Injectable()
export class DossierPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private dossierService: DossierService

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
                this.dossierService.find(id)
                    .subscribe((dossierResponse: HttpResponse<Dossier>) => {
                        const dossier: Dossier = dossierResponse.body;
                        if (dossier.dateCreation) {
                            dossier.dateCreation = {
                                year: dossier.dateCreation.getFullYear(),
                                month: dossier.dateCreation.getMonth() + 1,
                                day: dossier.dateCreation.getDate()
                            };
                        }
                        if (dossier.updatedAt) {
                            dossier.updatedAt = {
                                year: dossier.updatedAt.getFullYear(),
                                month: dossier.updatedAt.getMonth() + 1,
                                day: dossier.updatedAt.getDate()
                            };
                        }
                        if (dossier.dateCloture) {
                            dossier.dateCloture = {
                                year: dossier.dateCloture.getFullYear(),
                                month: dossier.dateCloture.getMonth() + 1,
                                day: dossier.dateCloture.getDate()
                            };
                        }
                        this.ngbModalRef = this.dossierModalRef(component, dossier);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.dossierModalRef(component, new Dossier());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    dossierModalRef(component: Component, dossier: Dossier): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.dossier = dossier;
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
