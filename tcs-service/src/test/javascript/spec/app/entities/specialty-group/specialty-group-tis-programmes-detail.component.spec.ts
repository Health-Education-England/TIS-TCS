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
import {SpecialtyGroupTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/specialty-group/specialty-group-tis-programmes-detail.component";
import {SpecialtyGroupTisProgrammesService} from "../../../../../../main/webapp/app/entities/specialty-group/specialty-group-tis-programmes.service";
import {SpecialtyGroupTisProgrammes} from "../../../../../../main/webapp/app/entities/specialty-group/specialty-group-tis-programmes.model";

describe('Component Tests', () => {

	describe('SpecialtyGroupTisProgrammes Management Detail Component', () => {
		let comp: SpecialtyGroupTisProgrammesDetailComponent;
		let fixture: ComponentFixture<SpecialtyGroupTisProgrammesDetailComponent>;
		let service: SpecialtyGroupTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [SpecialtyGroupTisProgrammesDetailComponent],
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
					SpecialtyGroupTisProgrammesService
				]
			}).overrideComponent(SpecialtyGroupTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(SpecialtyGroupTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(SpecialtyGroupTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new SpecialtyGroupTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.specialtyGroup).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
