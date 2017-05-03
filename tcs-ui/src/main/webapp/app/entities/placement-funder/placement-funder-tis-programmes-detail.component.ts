import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PlacementFunderTisProgrammes} from './placement-funder-tis-programmes.model';
import {PlacementFunderTisProgrammesService} from './placement-funder-tis-programmes.service';

@Component({
	selector: 'jhi-placement-funder-tis-programmes-detail',
	templateUrl: './placement-funder-tis-programmes-detail.component.html'
})
export class PlacementFunderTisProgrammesDetailComponent implements OnInit, OnDestroy {

	placementFunder: PlacementFunderTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private placementFunderService: PlacementFunderTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.addLocation('placementFunder');
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInPlacementFunders();
	}

	load(id) {
		this.placementFunderService.find(id).subscribe((placementFunder) => {
			this.placementFunder = placementFunder;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInPlacementFunders() {
		this.eventSubscriber = this.eventManager.subscribe('placementFunderListModification', (response) => this.load(this.placementFunder.id));
	}
}
