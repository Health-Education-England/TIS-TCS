import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {FundingComponentsTisProgrammes} from './funding-components-tis-programmes.model';
import {FundingComponentsTisProgrammesPopupService} from './funding-components-tis-programmes-popup.service';
import {FundingComponentsTisProgrammesService} from './funding-components-tis-programmes.service';

@Component({
	selector: 'jhi-funding-components-tis-programmes-delete-dialog',
	templateUrl: './funding-components-tis-programmes-delete-dialog.component.html'
})
export class FundingComponentsTisProgrammesDeleteDialogComponent {

	fundingComponents: FundingComponentsTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private fundingComponentsService: FundingComponentsTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('fundingComponents');
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.fundingComponentsService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'fundingComponentsListModification',
				content: 'Deleted an fundingComponents'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-funding-components-tis-programmes-delete-popup',
	template: ''
})
export class FundingComponentsTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingComponentsPopupService: FundingComponentsTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.fundingComponentsPopupService
			.open(FundingComponentsTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
