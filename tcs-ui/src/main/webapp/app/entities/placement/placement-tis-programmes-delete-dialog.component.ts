import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PlacementTisProgrammes} from './placement-tis-programmes.model';
import {PlacementTisProgrammesPopupService} from './placement-tis-programmes-popup.service';
import {PlacementTisProgrammesService} from './placement-tis-programmes.service';

@Component({
	selector: 'jhi-placement-tis-programmes-delete-dialog',
	templateUrl: './placement-tis-programmes-delete-dialog.component.html'
})
export class PlacementTisProgrammesDeleteDialogComponent {

	placement: PlacementTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private placementService: PlacementTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('placement');
		this.jhiLanguageService.addLocation('placementType');
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.placementService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'placementListModification',
				content: 'Deleted an placement'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-placement-tis-programmes-delete-popup',
	template: ''
})
export class PlacementTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private placementPopupService: PlacementTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.placementPopupService
			.open(PlacementTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
