import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PostTisProgrammes} from './post-tis-programmes.model';
import {PostTisProgrammesPopupService} from './post-tis-programmes-popup.service';
import {PostTisProgrammesService} from './post-tis-programmes.service';

@Component({
	selector: 'jhi-post-tis-programmes-delete-dialog',
	templateUrl: './post-tis-programmes-delete-dialog.component.html'
})
export class PostTisProgrammesDeleteDialogComponent {

	post: PostTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private postService: PostTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['post']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.postService.delete(id).subscribe((response) => {
			this.eventManager.broadcast({
				name: 'postListModification',
				content: 'Deleted an post'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-post-tis-programmes-delete-popup',
	template: ''
})
export class PostTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private postPopupService: PostTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			this.modalRef = this.postPopupService
			.open(PostTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
