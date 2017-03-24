import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {TrainingNumberTisProgrammes} from "./training-number-tis-programmes.model";
import {TrainingNumberTisProgrammesService} from "./training-number-tis-programmes.service";
import {Principal} from "../../shared";

@Component({
	selector: 'jhi-training-number-tis-programmes',
	templateUrl: './training-number-tis-programmes.component.html'
})
export class TrainingNumberTisProgrammesComponent implements OnInit, OnDestroy {
	trainingNumbers: TrainingNumberTisProgrammes[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trainingNumberService: TrainingNumberTisProgrammesService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['trainingNumber', 'trainingNumberType']);
	}

	loadAll() {
		this.trainingNumberService.query().subscribe(
			(res: Response) => {
				this.trainingNumbers = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInTrainingNumbers();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: TrainingNumberTisProgrammes) {
		return item.id;
	}


	registerChangeInTrainingNumbers() {
		this.eventSubscriber = this.eventManager.subscribe('trainingNumberListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
