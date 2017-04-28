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
import {CurriculumTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/curriculum/curriculum-tis-programmes-detail.component";
import {CurriculumTisProgrammesService} from "../../../../../../main/webapp/app/entities/curriculum/curriculum-tis-programmes.service";
import {CurriculumTisProgrammes} from "../../../../../../main/webapp/app/entities/curriculum/curriculum-tis-programmes.model";

describe('Component Tests', () => {

	describe('CurriculumTisProgrammes Management Detail Component', () => {
		let comp: CurriculumTisProgrammesDetailComponent;
		let fixture: ComponentFixture<CurriculumTisProgrammesDetailComponent>;
		let service: CurriculumTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [CurriculumTisProgrammesDetailComponent],
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
					CurriculumTisProgrammesService
				]
			}).overrideComponent(CurriculumTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(CurriculumTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(CurriculumTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new CurriculumTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.curriculum).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
