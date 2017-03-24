import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {GradeTisProgrammes} from "./grade-tis-programmes.model";
import {GradeTisProgrammesService} from "./grade-tis-programmes.service";

@Component({
	selector: 'jhi-grade-tis-programmes-detail',
	templateUrl: './grade-tis-programmes-detail.component.html'
})
export class GradeTisProgrammesDetailComponent implements OnInit, OnDestroy {

	grade: GradeTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gradeService: GradeTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['grade']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.gradeService.find(id).subscribe(grade => {
			this.grade = grade;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
