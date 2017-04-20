import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {TariffRateTisProgrammes} from './tariff-rate-tis-programmes.model';
import {TariffRateTisProgrammesService} from './tariff-rate-tis-programmes.service';

@Component({
	selector: 'jhi-tariff-rate-tis-programmes-detail',
	templateUrl: './tariff-rate-tis-programmes-detail.component.html'
})
export class TariffRateTisProgrammesDetailComponent implements OnInit, OnDestroy {

	tariffRate: TariffRateTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private tariffRateService: TariffRateTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['tariffRate']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInTariffRates();
	}

	load(id) {
		this.tariffRateService.find(id).subscribe((tariffRate) => {
			this.tariffRate = tariffRate;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInTariffRates() {
		this.eventSubscriber = this.eventManager.subscribe('tariffRateListModification', (response) => this.load(this.tariffRate.id));
	}
}
