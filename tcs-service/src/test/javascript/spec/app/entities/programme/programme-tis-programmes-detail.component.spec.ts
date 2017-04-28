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
import {ProgrammeTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/programme/programme-tis-programmes-detail.component";
import {ProgrammeTisProgrammesService} from "../../../../../../main/webapp/app/entities/programme/programme-tis-programmes.service";
import {ProgrammeTisProgrammes} from "../../../../../../main/webapp/app/entities/programme/programme-tis-programmes.model";

describe('Component Tests', () => {

	describe('ProgrammeTisProgrammes Management Detail Component', () => {
		let comp: ProgrammeTisProgrammesDetailComponent;
		let fixture: ComponentFixture<ProgrammeTisProgrammesDetailComponent>;
		let service: ProgrammeTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [ProgrammeTisProgrammesDetailComponent],
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
					ProgrammeTisProgrammesService
				]
			}).overrideComponent(ProgrammeTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(ProgrammeTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(ProgrammeTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new ProgrammeTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.programme).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
