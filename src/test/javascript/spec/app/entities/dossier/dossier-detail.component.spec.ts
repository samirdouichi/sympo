/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { DossierDetailComponent } from '../../../../../../main/webapp/app/entities/dossier/dossier-detail.component';
import { DossierService } from '../../../../../../main/webapp/app/entities/dossier/dossier.service';
import { Dossier } from '../../../../../../main/webapp/app/entities/dossier/dossier.model';

describe('Component Tests', () => {

    describe('Dossier Management Detail Component', () => {
        let comp: DossierDetailComponent;
        let fixture: ComponentFixture<DossierDetailComponent>;
        let service: DossierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DossierDetailComponent],
                providers: [
                    DossierService
                ]
            })
            .overrideTemplate(DossierDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DossierDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DossierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Dossier(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.dossier).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
