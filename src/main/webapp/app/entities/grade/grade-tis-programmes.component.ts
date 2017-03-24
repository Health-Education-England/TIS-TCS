import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {GradeTisProgrammes} from "./grade-tis-programmes.model";
import {GradeTisProgrammesService} from "./grade-tis-programmes.service";
import {Principal} from "../../shared";

@Component({
	selector: 'jhi-grade-tis-programmes',
	templateUrl: './grade-tis-programmes.component.html'
})
export class GradeTisProgrammesComponent implements OnInit, OnDestroy {
	grades: GradeTisProgrammes[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gradeService: GradeTisProgrammesService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['grade']);
	}

	loadAll() {
		this.gradeService.query().subscribe(
			(res: Response) => {
				this.grades = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInGrades();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: GradeTisProgrammes) {
		return item.id;
	}


	registerChangeInGrades() {
		this.eventSubscriber = this.eventManager.subscribe('gradeListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
