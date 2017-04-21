import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import {PostTisProgrammes} from './post-tis-programmes.model';
import {PostTisProgrammesPopupService} from './post-tis-programmes-popup.service';
import {PostTisProgrammesService} from './post-tis-programmes.service';

@Component({
	selector: 'jhi-post-tis-programmes-dialog',
	templateUrl: './post-tis-programmes-dialog.component.html'
})
export class PostTisProgrammesDialogComponent implements OnInit {

	post: PostTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	oldposts: PostTisProgrammes[];

	newposts: PostTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private postService: PostTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.addLocation('post');
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.postService.query({filter: 'post-is-null'}).subscribe((res: Response) => {
			if (!this.post.oldPostId) {
				this.oldposts = res.json();
			} else {
				this.postService.find(this.post.oldPostId).subscribe((subRes: PostTisProgrammes) => {
					this.oldposts = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
		this.postService.query({filter: 'post-is-null'}).subscribe((res: Response) => {
			if (!this.post.newPostId) {
				this.newposts = res.json();
			} else {
				this.postService.find(this.post.newPostId).subscribe((subRes: PostTisProgrammes) => {
					this.newposts = [subRes].concat(res.json());
				}, (subRes: Response) => this.onError(subRes.json()));
			}
		}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.post.id !== undefined) {
			this.postService.update(this.post)
			.subscribe((res: PostTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		} else {
			this.postService.create(this.post)
			.subscribe((res: PostTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
		}
	}

	private onSaveSuccess(result: PostTisProgrammes) {
		this.eventManager.broadcast({name: 'postListModification', content: 'OK'});
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

	trackPostById(index: number, item: PostTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-post-tis-programmes-popup',
	template: ''
})
export class PostTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private postPopupService: PostTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe((params) => {
			if (params['id']) {
				this.modalRef = this.postPopupService
				.open(PostTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.postPopupService
				.open(PostTisProgrammesDialogComponent);
			}
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
