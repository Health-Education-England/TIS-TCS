import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {SpecialtyTisProgrammes} from "./specialty-tis-programmes.model";
import {SpecialtyTisProgrammesService} from "./specialty-tis-programmes.service";

@Component({
	selector: 'jhi-specialty-tis-programmes-detail',
	templateUrl: './specialty-tis-programmes-detail.component.html'
})
export class SpecialtyTisProgrammesDetailComponent implements OnInit, OnDestroy {

	specialty: SpecialtyTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private specialtyService: SpecialtyTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['specialty', 'status', 'specialtyType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.specialtyService.find(id).subscribe(specialty => {
			this.specialty = specialty;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
