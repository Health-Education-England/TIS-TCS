import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {TariffRateTisProgrammes} from './tariff-rate-tis-programmes.model';
import {TariffRateTisProgrammesPopupService} from './tariff-rate-tis-programmes-popup.service';
import {TariffRateTisProgrammesService} from './tariff-rate-tis-programmes.service';

@Component({
	selector: 'jhi-tariff-rate-tis-programmes-dialog',
	templateUrl: './tariff-rate-tis-programmes-dialog.component.html'
})
export class TariffRateTisProgrammesDialogComponent implements OnInit {

	tariffRate: TariffRateTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private tariffRateService: TariffRateTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('tariffRate');
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.tariffRate.id !== undefined) {
			this.tariffRateService.update(this.tariffRate)
			.subscribe((res: TariffRateTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.tariffRateService.create(this.tariffRate)
			.subscribe((res: TariffRateTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: TariffRateTisProgrammes) {
		this.eventManager.broadcast({name: 'tariffRateListModification', content: 'OK'});
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
}

@Component({
	selector: 'jhi-tariff-rate-tis-programmes-popup',
	template: ''
})
export class TariffRateTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private tariffRatePopupService: TariffRateTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.tariffRatePopupService
				.open(TariffRateTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.tariffRatePopupService
				.open(TariffRateTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
