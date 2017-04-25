import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {FundingComponentsTisProgrammes} from './funding-components-tis-programmes.model';
import {FundingComponentsTisProgrammesService} from './funding-components-tis-programmes.service';

@Component({
	selector: 'jhi-funding-components-tis-programmes-detail',
	templateUrl: './funding-components-tis-programmes-detail.component.html'
})
export class FundingComponentsTisProgrammesDetailComponent implements OnInit, OnDestroy {

	fundingComponents: FundingComponentsTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private fundingComponentsService: FundingComponentsTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.addLocation('fundingComponents');
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInFundingComponents();
	}

	load(id) {
		this.fundingComponentsService.find(id).subscribe((fundingComponents) => {
			this.fundingComponents = fundingComponents;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInFundingComponents() {
		this.eventSubscriber = this.eventManager.subscribe('fundingComponentsListModification', (response) => this.load(this.fundingComponents.id));
	}
}
