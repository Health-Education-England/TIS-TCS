import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {PlacementTisProgrammes} from './placement-tis-programmes.model';
import {PlacementTisProgrammesPopupService} from './placement-tis-programmes-popup.service';
import {PlacementTisProgrammesService} from './placement-tis-programmes.service';

@Component({
	selector: 'jhi-placement-tis-programmes-dialog',
	templateUrl: './placement-tis-programmes-dialog.component.html'
})
export class PlacementTisProgrammesDialogComponent implements OnInit {

	placement: PlacementTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private placementService: PlacementTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['placement', 'placementType']);
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
		if (this.placement.id !== undefined) {
			this.placementService.update(this.placement)
			.subscribe((res: PlacementTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.placementService.create(this.placement)
			.subscribe((res: PlacementTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: PlacementTisProgrammes) {
		this.eventManager.broadcast({name: 'placementListModification', content: 'OK'});
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
	selector: 'jhi-placement-tis-programmes-popup',
	template: ''
})
export class PlacementTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private placementPopupService: PlacementTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.placementPopupService
				.open(PlacementTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.placementPopupService
				.open(PlacementTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
