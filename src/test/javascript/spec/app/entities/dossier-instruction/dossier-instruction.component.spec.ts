/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { DossierInstructionComponent } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.component';
import { DossierInstructionService } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.service';
import { DossierInstruction } from '../../../../../../main/webapp/app/entities/dossier-instruction/dossier-instruction.model';

describe('Component Tests', () => {

    describe('DossierInstruction Management Component', () => {
        let comp: DossierInstructionComponent;
        let fixture: ComponentFixture<DossierInstructionComponent>;
        let service: DossierInstructionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DossierInstructionComponent],
                providers: [
                    DossierInstructionService
                ]
            })
            .overrideTemplate(DossierInstructionComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DossierInstructionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DossierInstructionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DossierInstruction(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.dossierInstructions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
