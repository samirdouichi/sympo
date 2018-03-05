/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { PartenaireDetailComponent } from '../../../../../../main/webapp/app/entities/partenaire/partenaire-detail.component';
import { PartenaireService } from '../../../../../../main/webapp/app/entities/partenaire/partenaire.service';
import { Partenaire } from '../../../../../../main/webapp/app/entities/partenaire/partenaire.model';

describe('Component Tests', () => {

    describe('Partenaire Management Detail Component', () => {
        let comp: PartenaireDetailComponent;
        let fixture: ComponentFixture<PartenaireDetailComponent>;
        let service: PartenaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [PartenaireDetailComponent],
                providers: [
                    PartenaireService
                ]
            })
            .overrideTemplate(PartenaireDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PartenaireDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PartenaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Partenaire(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.partenaire).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
