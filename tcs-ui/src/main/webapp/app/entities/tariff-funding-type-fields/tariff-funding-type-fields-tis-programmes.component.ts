import {Component, OnInit, OnDestroy} from '@angular/core';
import {Response} from '@angular/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService} from 'ng-jhipster';

import {TariffFundingTypeFieldsTisProgrammes} from './tariff-funding-type-fields-tis-programmes.model';
import {TariffFundingTypeFieldsTisProgrammesService} from './tariff-funding-type-fields-tis-programmes.service';
import {ITEMS_PER_PAGE, Principal} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

@Component({
	selector: 'jhi-tariff-funding-type-fields-tis-programmes',
	templateUrl: './tariff-funding-type-fields-tis-programmes.component.html'
})
export class TariffFundingTypeFieldsTisProgrammesComponent implements OnInit, OnDestroy {
	tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes[];
	currentAccount: any;
	eventSubscriber: Subscription;

	constructor(private jhiLanguageService: JhiLanguageService,
				private tariffFundingTypeFieldsService: TariffFundingTypeFieldsTisProgrammesService,
				private alertService: AlertService,
				private eventManager: EventManager,
				private principal: Principal) {
		this.jhiLanguageService.addLocation('tariffFundingTypeFields');
	}

	loadAll() {
		this.tariffFundingTypeFieldsService.query().subscribe(
				(res: Response) => {
					this.tariffFundingTypeFields = res.json();
				},
				(res: Response) => this.onError(res.json())
		);
	}

	ngOnInit() {
		this.loadAll();
		this.principal.identity().then((account) => {
			this.currentAccount = account;
		});
		this.registerChangeInTariffFundingTypeFields();
	}

	ngOnDestroy() {
		this.eventManager.destroy(this.eventSubscriber);
	}

	trackId(index: number, item: TariffFundingTypeFieldsTisProgrammes) {
		return item.id;
	}

	registerChangeInTariffFundingTypeFields() {
		this.eventSubscriber = this.eventManager.subscribe('tariffFundingTypeFieldsListModification', (response) => this.loadAll());
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}
