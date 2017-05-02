import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {PostFundingTisProgrammes} from './post-funding-tis-programmes.model';
import {PostFundingTisProgrammesService} from './post-funding-tis-programmes.service';
@Injectable()
export class PostFundingTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private postFundingService: PostFundingTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.postFundingService.find(id).subscribe((postFunding) => {
				this.postFundingModalRef(component, postFunding);
			});
		} else {
			return this.postFundingModalRef(component, new PostFundingTisProgrammes());
		}
	}

	postFundingModalRef(component: Component, postFunding: PostFundingTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.postFunding = postFunding;
		modalRef.result.then((result) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
