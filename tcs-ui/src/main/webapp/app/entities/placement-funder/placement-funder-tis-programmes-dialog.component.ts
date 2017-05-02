import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {PlacementFunderTisProgrammes} from './placement-funder-tis-programmes.model';
import {PlacementFunderTisProgrammesPopupService} from './placement-funder-tis-programmes-popup.service';
import {PlacementFunderTisProgrammesService} from './placement-funder-tis-programmes.service';

@Component({
	selector: 'jhi-placement-funder-tis-programmes-dialog',
	templateUrl: './placement-funder-tis-programmes-dialog.component.html'
})
export class PlacementFunderTisProgrammesDialogComponent implements OnInit {

	placementFunder: PlacementFunderTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private placementFunderService: PlacementFunderTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('placementFunder');
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
		if (this.placementFunder.id !== undefined) {
			this.placementFunderService.update(this.placementFunder)
			.subscribe((res: PlacementFunderTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.placementFunderService.create(this.placementFunder)
			.subscribe((res: PlacementFunderTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: PlacementFunderTisProgrammes) {
		this.eventManager.broadcast({name: 'placementFunderListModification', content: 'OK'});
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
	selector: 'jhi-placement-funder-tis-programmes-popup',
	template: ''
})
export class PlacementFunderTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private placementFunderPopupService: PlacementFunderTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.placementFunderPopupService
				.open(PlacementFunderTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.placementFunderPopupService
				.open(PlacementFunderTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
