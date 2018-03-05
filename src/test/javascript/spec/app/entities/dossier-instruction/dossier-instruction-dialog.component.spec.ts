/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SympoTestModule } from '../../../test.module';
import { DossierInstructionDialogComponent } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction-dialog.component';
import { DossierInstructionService } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.service';
import { DossierInstruction } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.model';
import { CrediteurService } from '../../../../../../main/webapp/app/entities/crediteur';
import { DossierService } from '../../../../../../main/webapp/app/entities/dossier';

describe('Component Tests', () => {

    describe('DossierInstruction Management Dialog Component', () => {
        let comp: DossierInstructionDialogComponent;
        let fixture: ComponentFixture<DossierInstructionDialogComponent>;
        let service: DossierInstructionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DossierInstructionDialogComponent],
                providers: [
                    CrediteurService,
                    DossierService,
                    DossierInstructionService
                ]
            })
            .overrideTemplate(DossierInstructionDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DossierInstructionDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DossierInstructionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new DossierInstruction(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.dossierInstruction = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'dossierInstructionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new DossierInstruction();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.dossierInstruction = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'dossierInstructionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
