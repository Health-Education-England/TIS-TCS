import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {PostFundingTisProgrammes} from './post-funding-tis-programmes.model';
import {PostFundingTisProgrammesPopupService} from './post-funding-tis-programmes-popup.service';
import {PostFundingTisProgrammesService} from './post-funding-tis-programmes.service';
import {FundingTisProgrammes, FundingTisProgrammesService} from '../funding';
import {FundingComponentsTisProgrammes, FundingComponentsTisProgrammesService} from '../funding-components';

@Component({
	selector: 'jhi-post-funding-tis-programmes-dialog',
	templateUrl: './post-funding-tis-programmes-dialog.component.html'
})
export class PostFundingTisProgrammesDialogComponent implements OnInit {

	postFunding: PostFundingTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	fundings: FundingTisProgrammes[];

	fundingcomponents: FundingComponentsTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private postFundingService: PostFundingTisProgrammesService,
				private fundingService: FundingTisProgrammesService,
				private fundingComponentsService: FundingComponentsTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['postFunding']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.fundingService.query({filter: 'postfunding-is-null'}).subscribe((res: Response) => {
			if (!this.postFunding.fundingId) {
				this.fundings = res.json();
			} else {
				this.fundingService.find(this.postFunding.fundingId).subscribe((subRes: FundingTisProgrammes) => {
					this.fundings = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
		this.fundingComponentsService.query({filter: 'postfunding-is-null'}).subscribe((res: Response) => {
			if (!this.postFunding.fundingComponentsId) {
				this.fundingcomponents = res.json();
			} else {
				this.fundingComponentsService.find(this.postFunding.fundingComponentsId).subscribe((subRes: FundingComponentsTisProgrammes) => {
					this.fundingcomponents = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.postFunding.id !== undefined) {
			this.postFundingService.update(this.postFunding)
			.subscribe((res: PostFundingTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.postFundingService.create(this.postFunding)
			.subscribe((res: PostFundingTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: PostFundingTisProgrammes) {
		this.eventManager.broadcast({name: 'postFundingListModification', content: 'OK'});
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

	trackFundingById(index: number, item: FundingTisProgrammes) {
		return item.id;
	}

	trackFundingComponentsById(index: number, item: FundingComponentsTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-post-funding-tis-programmes-popup',
	template: ''
})
export class PostFundingTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private postFundingPopupService: PostFundingTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.postFundingPopupService
				.open(PostFundingTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.postFundingPopupService
				.open(PostFundingTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
