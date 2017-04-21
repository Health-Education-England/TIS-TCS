import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../../../test/javascript/spec/helpers/mock-language.service";
import {TisHeaderComponent} from "./tis-header.component";

describe('Tis Header', () => {
	describe('Component', () => {
		let comp: TisHeaderComponent;
		let fixture: ComponentFixture<TisHeaderComponent>;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [TisHeaderComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
				]
			}).overrideComponent(TisHeaderComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(TisHeaderComponent);
			comp = fixture.componentInstance;
		});


		describe('OnInit', () => {
			it('Should set a username', () => {
				// GIVEN

				// WHEN
				comp.ngOnInit();
				// THEN
				expect(comp.username).not.toEqual('');
			});
		});
	});

});
