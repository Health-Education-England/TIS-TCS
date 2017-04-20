import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PostTisProgrammes} from './post-tis-programmes.model';
import {PostTisProgrammesService} from './post-tis-programmes.service';

@Component({
	selector: 'jhi-post-tis-programmes-detail',
	templateUrl: './post-tis-programmes-detail.component.html'
})
export class PostTisProgrammesDetailComponent implements OnInit, OnDestroy {

	post: PostTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private postService: PostTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['post']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInPosts();
	}

	load(id) {
		this.postService.find(id).subscribe((post) => {
			this.post = post;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInPosts() {
		this.eventSubscriber = this.eventManager.subscribe('postListModification', (response) => this.load(this.post.id));
	}
}
