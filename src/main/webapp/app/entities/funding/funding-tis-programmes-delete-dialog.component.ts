import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {FundingTisProgrammes} from './funding-tis-programmes.model';
import {FundingTisProgrammesPopupService} from './funding-tis-programmes-popup.service';
import {FundingTisProgrammesService} from './funding-tis-programmes.service';

@Component({
	selector: 'jhi-funding-tis-programmes-delete-dialog',
	templateUrl: './funding-tis-programmes-delete-dialog.component.html'
})
export class FundingTisProgrammesDeleteDialogComponent {

	funding: FundingTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingService: FundingTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['funding', 'fundingType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.fundingService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'fundingListModification',
				content: 'Deleted an funding'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-funding-tis-programmes-delete-popup',
	template: ''
})
export class FundingTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingPopupService: FundingTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.fundingPopupService
			.open(FundingTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
