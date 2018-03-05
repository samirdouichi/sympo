/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { DossierInstructionDetailComponent } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction-detail.component';
import { DossierInstructionService } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.service';
import { DossierInstruction } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.model';

describe('Component Tests', () => {

    describe('DossierInstruction Management Detail Component', () => {
        let comp: DossierInstructionDetailComponent;
        let fixture: ComponentFixture<DossierInstructionDetailComponent>;
        let service: DossierInstructionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DossierInstructionDetailComponent],
                providers: [
                    DossierInstructionService
                ]
            })
            .overrideTemplate(DossierInstructionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DossierInstructionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DossierInstructionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DossierInstruction(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.dossierInstruction).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
