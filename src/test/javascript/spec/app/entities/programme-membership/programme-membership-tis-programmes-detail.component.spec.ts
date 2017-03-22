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
import {ProgrammeMembershipTisProgrammesDetailComponent} from "../../../../../../main/webapp/app/entities/programme-membership/programme-membership-tis-programmes-detail.component";
import {ProgrammeMembershipTisProgrammesService} from "../../../../../../main/webapp/app/entities/programme-membership/programme-membership-tis-programmes.service";
import {ProgrammeMembershipTisProgrammes} from "../../../../../../main/webapp/app/entities/programme-membership/programme-membership-tis-programmes.model";

describe('Component Tests', () => {

	describe('ProgrammeMembershipTisProgrammes Management Detail Component', () => {
		let comp: ProgrammeMembershipTisProgrammesDetailComponent;
		let fixture: ComponentFixture<ProgrammeMembershipTisProgrammesDetailComponent>;
		let service: ProgrammeMembershipTisProgrammesService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [ProgrammeMembershipTisProgrammesDetailComponent],
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
					ProgrammeMembershipTisProgrammesService
				]
			}).overrideComponent(ProgrammeMembershipTisProgrammesDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(ProgrammeMembershipTisProgrammesDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(ProgrammeMembershipTisProgrammesService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new ProgrammeMembershipTisProgrammes(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.programmeMembership).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
