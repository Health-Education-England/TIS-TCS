import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {TrainingNumberTisProgrammes} from "./training-number-tis-programmes.model";
import {TrainingNumberTisProgrammesService} from "./training-number-tis-programmes.service";

@Component({
	selector: 'jhi-training-number-tis-programmes-detail',
	templateUrl: './training-number-tis-programmes-detail.component.html'
})
export class TrainingNumberTisProgrammesDetailComponent implements OnInit, OnDestroy {

	trainingNumber: TrainingNumberTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trainingNumberService: TrainingNumberTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['trainingNumber', 'trainingNumberType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.trainingNumberService.find(id).subscribe(trainingNumber => {
			this.trainingNumber = trainingNumber;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
