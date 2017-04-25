import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {FundingTisProgrammes} from './funding-tis-programmes.model';
import {FundingTisProgrammesPopupService} from './funding-tis-programmes-popup.service';
import {FundingTisProgrammesService} from './funding-tis-programmes.service';

@Component({
	selector: 'jhi-funding-tis-programmes-dialog',
	templateUrl: './funding-tis-programmes-dialog.component.html'
})
export class FundingTisProgrammesDialogComponent implements OnInit {

	funding: FundingTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private fundingService: FundingTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('funding');
		this.jhiLanguageService.addLocation('fundingType');
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
		if (this.funding.id !== undefined) {
			this.fundingService.update(this.funding)
			.subscribe((res: FundingTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.fundingService.create(this.funding)
			.subscribe((res: FundingTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: FundingTisProgrammes) {
		this.eventManager.broadcast({name: 'fundingListModification', content: 'OK'});
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
	selector: 'jhi-funding-tis-programmes-popup',
	template: ''
})
export class FundingTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingPopupService: FundingTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.fundingPopupService
				.open(FundingTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.fundingPopupService
				.open(FundingTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
