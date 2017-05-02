import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {SpecialtyTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/specialty/specialty-tis-programmes-detail.component";
import {SpecialtyTisProgrammesService} from "../../../../../../main/webapp/app/entities/specialty/specialty-tis-programmes.service";
import {SpecialtyTisProgrammes} from "../../../../../../main/webapp/app/entities/specialty/specialty-tis-programmes.model";

describe('Component Tests', () => {

	describe('SpecialtyTisProgrammes Management Detail Component', () => {
		let comp: SpecialtyTisProgrammesDetailComponent;
		let fixture: ComponentFixture<SpecialtyTisProgrammesDetailComponent>;
		let service: SpecialtyTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [SpecialtyTisProgrammesDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					SpecialtyTisProgrammesService
				]
			}).overrideComponent(SpecialtyTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(SpecialtyTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(SpecialtyTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new SpecialtyTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.specialty).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
