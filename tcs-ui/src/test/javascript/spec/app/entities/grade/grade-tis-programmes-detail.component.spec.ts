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
import {GradeTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/grade/grade-tis-programmes-detail.component";
import {GradeTisProgrammesService} from "../../../../../../main/webapp/app/entities/grade/grade-tis-programmes.service";
import {GradeTisProgrammes} from "../../../../../../main/webapp/app/entities/grade/grade-tis-programmes.model";

describe('Component Tests', () => {

	describe('GradeTisProgrammes Management Detail Component', () => {
		let comp: GradeTisProgrammesDetailComponent;
		let fixture: ComponentFixture<GradeTisProgrammesDetailComponent>;
		let service: GradeTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [GradeTisProgrammesDetailComponent],
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
					GradeTisProgrammesService
				]
			}).overrideComponent(GradeTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(GradeTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(GradeTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new GradeTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.grade).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
