import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {ProgrammeMembershipTisProgrammes} from "./programme-membership-tis-programmes.model";
import {ProgrammeMembershipTisProgrammesService} from "./programme-membership-tis-programmes.service";

@Component({
	selector: 'jhi-programme-membership-tis-programmes-detail',
	templateUrl: './programme-membership-tis-programmes-detail.component.html'
})
export class ProgrammeMembershipTisProgrammesDetailComponent implements OnInit, OnDestroy {

	programmeMembership: ProgrammeMembershipTisProgrammes;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['programmeMembership', 'programmeMembershipType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.programmeMembershipService.find(id).subscribe(programmeMembership => {
			this.programmeMembership = programmeMembership;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
