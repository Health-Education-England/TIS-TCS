import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {FundingTisProgrammes} from './funding-tis-programmes.model';
import {FundingTisProgrammesService} from './funding-tis-programmes.service';
@Injectable()
export class FundingTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private fundingService: FundingTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.fundingService.find(id).subscribe((funding) => {
				if (funding.startDate) {
					funding.startDate = {
						year: funding.startDate.getFullYear(),
						month: funding.startDate.getMonth() + 1,
						day: funding.startDate.getDate()
					};
				}
				if (funding.endDate) {
					funding.endDate = {
						year: funding.endDate.getFullYear(),
						month: funding.endDate.getMonth() + 1,
						day: funding.endDate.getDate()
					};
				}
				this.fundingModalRef(component, funding);
			});
		} else {
			return this.fundingModalRef(component, new FundingTisProgrammes());
		}
	}

	fundingModalRef(component: Component, funding: FundingTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.funding = funding;
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
