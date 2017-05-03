import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PostFundingTisProgrammes} from './post-funding-tis-programmes.model';
import {PostFundingTisProgrammesPopupService} from './post-funding-tis-programmes-popup.service';
import {PostFundingTisProgrammesService} from './post-funding-tis-programmes.service';

@Component({
	selector: 'jhi-post-funding-tis-programmes-delete-dialog',
	templateUrl: './post-funding-tis-programmes-delete-dialog.component.html'
})
export class PostFundingTisProgrammesDeleteDialogComponent {

	postFunding: PostFundingTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private postFundingService: PostFundingTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('postFunding');
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.postFundingService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'postFundingListModification',
				content: 'Deleted an postFunding'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-post-funding-tis-programmes-delete-popup',
	template: ''
})
export class PostFundingTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private postFundingPopupService: PostFundingTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.postFundingPopupService
			.open(PostFundingTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
