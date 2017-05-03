import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {FundingTisProgrammes} from './funding-tis-programmes.model';
import {FundingTisProgrammesService} from './funding-tis-programmes.service';

@Component({
	selector: 'jhi-funding-tis-programmes-detail',
	templateUrl: './funding-tis-programmes-detail.component.html'
})
export class FundingTisProgrammesDetailComponent implements OnInit, OnDestroy {

	funding: FundingTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private fundingService: FundingTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.addLocation('funding');
		this.jhiLanguageService.addLocation('fundingType');
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInFundings();
	}

	load(id) {
		this.fundingService.find(id).subscribe((funding) => {
			this.funding = funding;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInFundings() {
		this.eventSubscriber = this.eventManager.subscribe('fundingListModification', (response) => this.load(this.funding.id));
	}
}
