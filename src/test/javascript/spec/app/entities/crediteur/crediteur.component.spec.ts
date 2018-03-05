/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CrediteurComponent } from '../../../../../../main/webapp/app/entities/crediteur/crediteur.component';
import { CrediteurService } from '../../../../../../main/webapp/app/entities/crediteur/crediteur.service';
import { Crediteur } from '../../../../../../main/webapp/app/entities/crediteur/crediteur.model';

describe('Component Tests', () => {

    describe('Crediteur Management Component', () => {
        let comp: CrediteurComponent;
        let fixture: ComponentFixture<CrediteurComponent>;
        let service: CrediteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CrediteurComponent],
                providers: [
                    CrediteurService
                ]
            })
            .overrideTemplate(CrediteurComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CrediteurComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CrediteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Crediteur(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.crediteurs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
