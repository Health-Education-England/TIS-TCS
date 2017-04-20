import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {EventManager, JhiLanguageService} from 'ng-jhipster';

import {PostFundingTisProgrammes} from './post-funding-tis-programmes.model';
import {PostFundingTisProgrammesService} from './post-funding-tis-programmes.service';

@Component({
	selector: 'jhi-post-funding-tis-programmes-detail',
	templateUrl: './post-funding-tis-programmes-detail.component.html'
})
export class PostFundingTisProgrammesDetailComponent implements OnInit, OnDestroy {

	postFunding: PostFundingTisProgrammes;
	private subscription: any;
	private eventSubscriber: Subscription;

	constructor(private eventManager: EventManager,
				private jhiLanguageService: JhiLanguageService,
				private postFundingService: PostFundingTisProgrammesService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['postFunding']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe((params) => {
			this.load(params['id']);
		});
		this.registerChangeInPostFundings();
	}

	load(id) {
		this.postFundingService.find(id).subscribe((postFunding) => {
			this.postFunding = postFunding;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
		this.eventManager.destroy(this.eventSubscriber);
	}

	registerChangeInPostFundings() {
		this.eventSubscriber = this.eventManager.subscribe('postFundingListModification', (response) => this.load(this.postFunding.id));
	}
}
