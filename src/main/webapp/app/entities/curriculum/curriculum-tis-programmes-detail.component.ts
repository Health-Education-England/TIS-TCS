import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {CurriculumTisProgrammes} from "./curriculum-tis-programmes.model";
import {CurriculumTisProgrammesService} from "./curriculum-tis-programmes.service";

@Component({
	selector: 'jhi-curriculum-tis-programmes-detail',
	templateUrl: './curriculum-tis-programmes-detail.component.html'
})
export class CurriculumTisProgrammesDetailComponent implements OnInit, OnDestroy {

	curriculum: CurriculumTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private curriculumService: CurriculumTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['curriculum', 'curriculumSubType', 'assessmentType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.curriculumService.find(id).subscribe(curriculum => {
			this.curriculum = curriculum;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
