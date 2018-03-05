import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CategorieDossier } from './categorie-dossier.model';
import { CategorieDossierService } from './categorie-dossier.service';

@Injectable()
export class CategorieDossierPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private categorieDossierService: CategorieDossierService

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
                this.categorieDossierService.find(id)
                    .subscribe((categorieDossierResponse: HttpResponse<CategorieDossier>) => {
                        const categorieDossier: CategorieDossier = categorieDossierResponse.body;
                        this.ngbModalRef = this.categorieDossierModalRef(component, categorieDossier);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.categorieDossierModalRef(component, new CategorieDossier());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    categorieDossierModalRef(component: Component, categorieDossier: CategorieDossier): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.categorieDossier = categorieDossier;
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
