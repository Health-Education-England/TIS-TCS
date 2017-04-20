import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {TariffFundingTypeFieldsTisProgrammes} from './tariff-funding-type-fields-tis-programmes.model';
import {TariffFundingTypeFieldsTisProgrammesPopupService} from './tariff-funding-type-fields-tis-programmes-popup.service';
import {TariffFundingTypeFieldsTisProgrammesService} from './tariff-funding-type-fields-tis-programmes.service';

@Component({
	selector: 'jhi-tariff-funding-type-fields-tis-programmes-delete-dialog',
	templateUrl: './tariff-funding-type-fields-tis-programmes-delete-dialog.component.html'
})
export class TariffFundingTypeFieldsTisProgrammesDeleteDialogComponent {

	tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private tariffFundingTypeFieldsService: TariffFundingTypeFieldsTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['tariffFundingTypeFields']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.tariffFundingTypeFieldsService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'tariffFundingTypeFieldsListModification',
				content: 'Deleted an tariffFundingTypeFields'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-tariff-funding-type-fields-tis-programmes-delete-popup',
	template: ''
})
export class TariffFundingTypeFieldsTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private tariffFundingTypeFieldsPopupService: TariffFundingTypeFieldsTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.tariffFundingTypeFieldsPopupService
			.open(TariffFundingTypeFieldsTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
