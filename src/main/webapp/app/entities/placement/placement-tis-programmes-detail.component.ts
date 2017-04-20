import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PlacementTisProgrammes} from './placement-tis-programmes.model';
import {PlacementTisProgrammesService} from './placement-tis-programmes.service';

@Component({
	selector: 'jhi-placement-tis-programmes-detail',
	templateUrl: './placement-tis-programmes-detail.component.html'
})
export class PlacementTisProgrammesDetailComponent implements OnInit, OnDestroy {

	placement: PlacementTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private placementService: PlacementTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['placement', 'placementType']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInPlacements();
	}

	load(id) {
		this.placementService.find(id).subscribe((placement) => {
			this.placement = placement;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInPlacements() {
		this.eventSubscriber = this.eventManager.subscribe('placementListModification', (response) => this.load(this.placement.id));
	}
}
