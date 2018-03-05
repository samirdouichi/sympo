/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CategorieDossierComponent } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.component';
import { CategorieDossierService } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.service';
import { CategorieDossier } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.model';

describe('Component Tests', () => {

    describe('CategorieDossier Management Component', () => {
        let comp: CategorieDossierComponent;
        let fixture: ComponentFixture<CategorieDossierComponent>;
        let service: CategorieDossierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CategorieDossierComponent],
                providers: [
                    CategorieDossierService
                ]
            })
            .overrideTemplate(CategorieDossierComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategorieDossierComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategorieDossierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CategorieDossier(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.categorieDossiers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
