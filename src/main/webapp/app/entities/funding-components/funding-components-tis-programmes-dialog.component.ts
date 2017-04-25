import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {FundingComponentsTisProgrammes} from './funding-components-tis-programmes.model';
import {FundingComponentsTisProgrammesPopupService} from './funding-components-tis-programmes-popup.service';
import {FundingComponentsTisProgrammesService} from './funding-components-tis-programmes.service';
import {PlacementFunderTisProgrammes, PlacementFunderTisProgrammesService} from '../placement-funder';

@Component({
	selector: 'jhi-funding-components-tis-programmes-dialog',
	templateUrl: './funding-components-tis-programmes-dialog.component.html'
})
export class FundingComponentsTisProgrammesDialogComponent implements OnInit {

	fundingComponents: FundingComponentsTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	fundingorganisations: PlacementFunderTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private fundingComponentsService: FundingComponentsTisProgrammesService,
				private placementFunderService: PlacementFunderTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('fundingComponents');
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.placementFunderService.query({filter: 'fundingcomponents-is-null'}).subscribe((res: Response) => {
			if (!this.fundingComponents.fundingOrganisationId) {
				this.fundingorganisations = res.json();
			} else {
				this.placementFunderService.find(this.fundingComponents.fundingOrganisationId).subscribe((subRes: PlacementFunderTisProgrammes) => {
					this.fundingorganisations = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.fundingComponents.id !== undefined) {
			this.fundingComponentsService.update(this.fundingComponents)
			.subscribe((res: FundingComponentsTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.fundingComponentsService.create(this.fundingComponents)
			.subscribe((res: FundingComponentsTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: FundingComponentsTisProgrammes) {
		this.eventManager.broadcast({name: 'fundingComponentsListModification', content: 'OK'});
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

	trackPlacementFunderById(index: number, item: PlacementFunderTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-funding-components-tis-programmes-popup',
	template: ''
})
export class FundingComponentsTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private fundingComponentsPopupService: FundingComponentsTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.fundingComponentsPopupService
				.open(FundingComponentsTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.fundingComponentsPopupService
				.open(FundingComponentsTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
