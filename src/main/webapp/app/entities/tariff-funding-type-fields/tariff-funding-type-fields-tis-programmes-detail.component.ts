import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {TariffFundingTypeFieldsTisProgrammes} from './tariff-funding-type-fields-tis-programmes.model';
import {TariffFundingTypeFieldsTisProgrammesService} from './tariff-funding-type-fields-tis-programmes.service';

@Component({
	selector: 'jhi-tariff-funding-type-fields-tis-programmes-detail',
	templateUrl: './tariff-funding-type-fields-tis-programmes-detail.component.html'
})
export class TariffFundingTypeFieldsTisProgrammesDetailComponent implements OnInit, OnDestroy {

	tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private tariffFundingTypeFieldsService: TariffFundingTypeFieldsTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.addLocation('tariffFundingTypeFields');
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInTariffFundingTypeFields();
	}

	load(id) {
		this.tariffFundingTypeFieldsService.find(id).subscribe((tariffFundingTypeFields) => {
			this.tariffFundingTypeFields = tariffFundingTypeFields;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInTariffFundingTypeFields() {
		this.eventSubscriber = this.eventManager.subscribe('tariffFundingTypeFieldsListModification', (response) => this.load(this.tariffFundingTypeFields.id));
	}
}
