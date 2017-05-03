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
import {TrainingNumberTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/training-number/training-number-tis-programmes-detail.component";
import {TrainingNumberTisProgrammesService} from "../../../../../../main/webapp/app/entities/training-number/training-number-tis-programmes.service";
import {TrainingNumberTisProgrammes} from "../../../../../../main/webapp/app/entities/training-number/training-number-tis-programmes.model";

describe('Component Tests', () => {

	describe('TrainingNumberTisProgrammes Management Detail Component', () => {
		let comp: TrainingNumberTisProgrammesDetailComponent;
		let fixture: ComponentFixture<TrainingNumberTisProgrammesDetailComponent>;
		let service: TrainingNumberTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [TrainingNumberTisProgrammesDetailComponent],
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
					TrainingNumberTisProgrammesService
				]
			}).overrideComponent(TrainingNumberTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(TrainingNumberTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(TrainingNumberTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new TrainingNumberTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.trainingNumber).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
