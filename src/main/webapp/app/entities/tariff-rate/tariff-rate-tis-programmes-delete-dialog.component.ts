import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {TariffRateTisProgrammes} from './tariff-rate-tis-programmes.model';
import {TariffRateTisProgrammesPopupService} from './tariff-rate-tis-programmes-popup.service';
import {TariffRateTisProgrammesService} from './tariff-rate-tis-programmes.service';

@Component({
	selector: 'jhi-tariff-rate-tis-programmes-delete-dialog',
	templateUrl: './tariff-rate-tis-programmes-delete-dialog.component.html'
})
export class TariffRateTisProgrammesDeleteDialogComponent {

	tariffRate: TariffRateTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private tariffRateService: TariffRateTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['tariffRate']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.tariffRateService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'tariffRateListModification',
				content: 'Deleted an tariffRate'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-tariff-rate-tis-programmes-delete-popup',
	template: ''
})
export class TariffRateTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private tariffRatePopupService: TariffRateTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.tariffRatePopupService
			.open(TariffRateTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
