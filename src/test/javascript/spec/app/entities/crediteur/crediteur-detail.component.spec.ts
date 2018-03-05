/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CrediteurDetailComponent } from '../../../../../../main/webapp/app/entities/crediteur/crediteur-detail.component';
import { CrediteurService } from '../../../../../../main/webapp/app/entities/crediteur/crediteur.service';
import { Crediteur } from '../../../../../../main/webapp/app/entities/crediteur/crediteur.model';

describe('Component Tests', () => {

    describe('Crediteur Management Detail Component', () => {
        let comp: CrediteurDetailComponent;
        let fixture: ComponentFixture<CrediteurDetailComponent>;
        let service: CrediteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CrediteurDetailComponent],
                providers: [
                    CrediteurService
                ]
            })
            .overrideTemplate(CrediteurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CrediteurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CrediteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Crediteur(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.crediteur).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
