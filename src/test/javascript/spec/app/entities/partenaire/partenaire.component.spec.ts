/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { PartenaireComponent } from '../../../../../../main/webapp/app/entities/partenaire/partenaire.component';
import { PartenaireService } from '../../../../../../main/webapp/app/entities/partenaire/partenaire.service';
import { Partenaire } from '../../../../../../main/webapp/app/entities/partenaire/partenaire.model';

describe('Component Tests', () => {

    describe('Partenaire Management Component', () => {
        let comp: PartenaireComponent;
        let fixture: ComponentFixture<PartenaireComponent>;
        let service: PartenaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [PartenaireComponent],
                providers: [
                    PartenaireService
                ]
            })
            .overrideTemplate(PartenaireComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PartenaireComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PartenaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Partenaire(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.partenaires[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
