/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SympoTestModule } from '../../../test.module';
import { CategorieDossierDialogComponent } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier-dialog.component';
import { CategorieDossierService } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.service';
import { CategorieDossier } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.model';
import { DossierService } from '../../../../../../main/webapp/app/entities/dossier';

describe('Component Tests', () => {

    describe('CategorieDossier Management Dialog Component', () => {
        let comp: CategorieDossierDialogComponent;
        let fixture: ComponentFixture<CategorieDossierDialogComponent>;
        let service: CategorieDossierService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CategorieDossierDialogComponent],
                providers: [
                    DossierService,
                    CategorieDossierService
                ]
            })
            .overrideTemplate(CategorieDossierDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategorieDossierDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategorieDossierService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CategorieDossier(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.categorieDossier = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'categorieDossierListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CategorieDossier();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.categorieDossier = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'categorieDossierListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
