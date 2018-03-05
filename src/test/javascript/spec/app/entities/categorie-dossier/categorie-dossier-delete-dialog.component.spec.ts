/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SympoTestModule } from '../../../test.module';
import { CategorieDossierDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier-delete-dialog.component';
import { CategorieDossierService } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.service';

describe('Component Tests', () => {

    describe('CategorieDossier Management Delete Component', () => {
        let comp: CategorieDossierDeleteDialogComponent;
        let fixture: ComponentFixture<CategorieDossierDeleteDialogComponent>;
        let service: CategorieDossierService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CategorieDossierDeleteDialogComponent],
                providers: [
                    CategorieDossierService
                ]
            })
            .overrideTemplate(CategorieDossierDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategorieDossierDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategorieDossierService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
