/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { DebiteurComponent } from '../../../../../../main/webapp/app/entities/debiteur/debiteur.component';
import { DebiteurService } from '../../../../../../main/webapp/app/entities/debiteur/debiteur.service';
import { Debiteur } from '../../../../../../main/webapp/app/entities/debiteur/debiteur.model';

describe('Component Tests', () => {

    describe('Debiteur Management Component', () => {
        let comp: DebiteurComponent;
        let fixture: ComponentFixture<DebiteurComponent>;
        let service: DebiteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DebiteurComponent],
                providers: [
                    DebiteurService
                ]
            })
            .overrideTemplate(DebiteurComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DebiteurComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DebiteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Debiteur(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.debiteurs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
