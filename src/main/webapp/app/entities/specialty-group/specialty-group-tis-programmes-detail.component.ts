import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {SpecialtyGroupTisProgrammes} from "./specialty-group-tis-programmes.model";
import {SpecialtyGroupTisProgrammesService} from "./specialty-group-tis-programmes.service";

@Component({
	selector: 'jhi-specialty-group-tis-programmes-detail',
	templateUrl: './specialty-group-tis-programmes-detail.component.html'
})
export class SpecialtyGroupTisProgrammesDetailComponent implements OnInit, OnDestroy {

	specialtyGroup: SpecialtyGroupTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private specialtyGroupService: SpecialtyGroupTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['specialtyGroup']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.specialtyGroupService.find(id).subscribe(specialtyGroup => {
			this.specialtyGroup = specialtyGroup;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
