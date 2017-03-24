import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {ProgrammeTisProgrammes} from "./programme-tis-programmes.model";
import {ProgrammeTisProgrammesService} from "./programme-tis-programmes.service";

@Component({
	selector: 'jhi-programme-tis-programmes-detail',
	templateUrl: './programme-tis-programmes-detail.component.html'
})
export class ProgrammeTisProgrammesDetailComponent implements OnInit, OnDestroy {

	programme: ProgrammeTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeService: ProgrammeTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['programme', 'status']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.programmeService.find(id).subscribe(programme => {
			this.programme = programme;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
