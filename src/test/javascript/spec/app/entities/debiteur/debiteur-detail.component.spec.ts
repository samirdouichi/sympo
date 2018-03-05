/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { DebiteurDetailComponent } from '../../../../../../main/webapp/app/entities/debiteur/debiteur-detail.component';
import { DebiteurService } from '../../../../../../main/webapp/app/entities/debiteur/debiteur.service';
import { Debiteur } from '../../../../../../main/webapp/app/entities/debiteur/debiteur.model';

describe('Component Tests', () => {

    describe('Debiteur Management Detail Component', () => {
        let comp: DebiteurDetailComponent;
        let fixture: ComponentFixture<DebiteurDetailComponent>;
        let service: DebiteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [DebiteurDetailComponent],
                providers: [
                    DebiteurService
                ]
            })
            .overrideTemplate(DebiteurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DebiteurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DebiteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Debiteur(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.debiteur).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
