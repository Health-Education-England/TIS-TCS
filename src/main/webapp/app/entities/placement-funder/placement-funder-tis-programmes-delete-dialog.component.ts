import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PlacementFunderTisProgrammes} from './placement-funder-tis-programmes.model';
import {PlacementFunderTisProgrammesPopupService} from './placement-funder-tis-programmes-popup.service';
import {PlacementFunderTisProgrammesService} from './placement-funder-tis-programmes.service';

@Component({
	selector: 'jhi-placement-funder-tis-programmes-delete-dialog',
	templateUrl: './placement-funder-tis-programmes-delete-dialog.component.html'
})
export class PlacementFunderTisProgrammesDeleteDialogComponent {

	placementFunder: PlacementFunderTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private placementFunderService: PlacementFunderTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['placementFunder']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.placementFunderService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'placementFunderListModification',
				content: 'Deleted an placementFunder'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-placement-funder-tis-programmes-delete-popup',
	template: ''
})
export class PlacementFunderTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private placementFunderPopupService: PlacementFunderTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.placementFunderPopupService
			.open(PlacementFunderTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
