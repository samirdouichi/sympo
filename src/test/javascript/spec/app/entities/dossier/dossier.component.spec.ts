/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { DossierComponent } from '../../../../../../main/webapp/app/entities/dossier/dossier.component';
import { DossierService } from '../../../../../../main/webapp/app/entities/dossier/dossier.service';
import { Dossier } from '../../../../../../main/webapp/app/entities/dossier/dossier.model';

describe('Component Tests', () => {

    describe('Dossier Management Component', () => {
        let comp: DossierComponent;
        let fixture: ComponentFixture<DossierComponent>;
        let service: DossierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DossierComponent],
                providers: [
                    DossierService
                ]
            })
            .overrideTemplate(DossierComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DossierComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DossierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Dossier(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.dossiers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
