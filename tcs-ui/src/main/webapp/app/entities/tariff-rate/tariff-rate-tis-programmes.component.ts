import {Component, OnInit, OnDestroy} from '@angular/core';
import {Response} from '@angular/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService} from 'ng-jhipster';

import {TariffRateTisProgrammes} from './tariff-rate-tis-programmes.model';
import {TariffRateTisProgrammesService} from './tariff-rate-tis-programmes.service';
import {ITEMS_PER_PAGE, Principal} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
	selector: 'jhi-tariff-rate-tis-programmes',
	templateUrl: './tariff-rate-tis-programmes.component.html'
})
export class TariffRateTisProgrammesComponent implements OnInit, OnDestroy {
	tariffRates: TariffRateTisProgrammes[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private tariffRateService: TariffRateTisProgrammesService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.addLocation('tariffRate');
	}

	loadAll() {
		this.tariffRateService.query().subscribe(
				(res: Response) => {
					this.tariffRates = res.json();
				},
				(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInTariffRates();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: TariffRateTisProgrammes) {
		return item.id;
	}

	registerChangeInTariffRates() {
		this.eventSubscriber = this.eventManager.subscribe('tariffRateListModification', (response) => this.loadAll());
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
