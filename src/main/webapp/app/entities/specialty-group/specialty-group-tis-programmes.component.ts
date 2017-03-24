import {Component, OnInit, OnDestroy} from "@angular/core";
import {Response} from "@angular/http";
import {Subscription} from "rxjs/Rx";
import {EventManager, JhiLanguageService, AlertService} from "ng-jhipster";
import {SpecialtyGroupTisProgrammes} from "./specialty-group-tis-programmes.model";
import {SpecialtyGroupTisProgrammesService} from "./specialty-group-tis-programmes.service";
import {Principal} from "../../shared";

@Component({
	selector: 'jhi-specialty-group-tis-programmes',
	templateUrl: './specialty-group-tis-programmes.component.html'
})
export class SpecialtyGroupTisProgrammesComponent implements OnInit, OnDestroy {
	specialtyGroups: SpecialtyGroupTisProgrammes[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private specialtyGroupService: SpecialtyGroupTisProgrammesService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.setLocations(['specialtyGroup']);
	}

	loadAll() {
		this.specialtyGroupService.query().subscribe(
			(res: Response) => {
				this.specialtyGroups = res.json();
			},
			(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInSpecialtyGroups();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: SpecialtyGroupTisProgrammes) {
		return item.id;
	}


	registerChangeInSpecialtyGroups() {
		this.eventSubscriber = this.eventManager.subscribe('specialtyGroupListModification', (response) => this.loadAll());
	}


	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
