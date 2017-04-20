import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {TariffFundingTypeFieldsTisProgrammes} from './tariff-funding-type-fields-tis-programmes.model';
import {TariffFundingTypeFieldsTisProgrammesPopupService} from './tariff-funding-type-fields-tis-programmes-popup.service';
import {TariffFundingTypeFieldsTisProgrammesService} from './tariff-funding-type-fields-tis-programmes.service';
import {TariffRateTisProgrammes, TariffRateTisProgrammesService} from '../tariff-rate';
import {PlacementFunderTisProgrammes, PlacementFunderTisProgrammesService} from '../placement-funder';

@Component({
	selector: 'jhi-tariff-funding-type-fields-tis-programmes-dialog',
	templateUrl: './tariff-funding-type-fields-tis-programmes-dialog.component.html'
})
export class TariffFundingTypeFieldsTisProgrammesDialogComponent implements OnInit {

	tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	levelofposts: TariffRateTisProgrammes[];

	placementratefundedbies: PlacementFunderTisProgrammes[];

	placementrateprovidedtos: PlacementFunderTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private tariffFundingTypeFieldsService: TariffFundingTypeFieldsTisProgrammesService,
				private tariffRateService: TariffRateTisProgrammesService,
				private placementFunderService: PlacementFunderTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['tariffFundingTypeFields']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.tariffRateService.query({filter: 'tarifffundingtypefields-is-null'}).subscribe((res: Response) => {
			if (!this.tariffFundingTypeFields.levelOfPostId) {
				this.levelofposts = res.json();
			} else {
				this.tariffRateService.find(this.tariffFundingTypeFields.levelOfPostId).subscribe((subRes: TariffRateTisProgrammes) => {
					this.levelofposts = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
		this.placementFunderService.query({filter: 'tarifffundingtypefields-is-null'}).subscribe((res: Response) => {
			if (!this.tariffFundingTypeFields.placementRateFundedById) {
				this.placementratefundedbies = res.json();
			} else {
				this.placementFunderService.find(this.tariffFundingTypeFields.placementRateFundedById).subscribe((subRes: PlacementFunderTisProgrammes) => {
					this.placementratefundedbies = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
		this.placementFunderService.query({filter: 'tarifffundingtypefields-is-null'}).subscribe((res: Response) => {
			if (!this.tariffFundingTypeFields.placementRateProvidedToId) {
				this.placementrateprovidedtos = res.json();
			} else {
				this.placementFunderService.find(this.tariffFundingTypeFields.placementRateProvidedToId).subscribe((subRes: PlacementFunderTisProgrammes) => {
					this.placementrateprovidedtos = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.tariffFundingTypeFields.id !== undefined) {
			this.tariffFundingTypeFieldsService.update(this.tariffFundingTypeFields)
			.subscribe((res: TariffFundingTypeFieldsTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.tariffFundingTypeFieldsService.create(this.tariffFundingTypeFields)
			.subscribe((res: TariffFundingTypeFieldsTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: TariffFundingTypeFieldsTisProgrammes) {
		this.eventManager.broadcast({name: 'tariffFundingTypeFieldsListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		try {
			error.json();
		} catch (exception) {
			error.message = error.text();
		}
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}

	trackTariffRateById(index: number, item: TariffRateTisProgrammes) {
		return item.id;
	}

	trackPlacementFunderById(index: number, item: PlacementFunderTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-tariff-funding-type-fields-tis-programmes-popup',
	template: ''
})
export class TariffFundingTypeFieldsTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private tariffFundingTypeFieldsPopupService: TariffFundingTypeFieldsTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.tariffFundingTypeFieldsPopupService
				.open(TariffFundingTypeFieldsTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.tariffFundingTypeFieldsPopupService
				.open(TariffFundingTypeFieldsTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
