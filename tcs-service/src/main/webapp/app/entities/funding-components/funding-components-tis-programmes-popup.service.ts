import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {FundingComponentsTisProgrammes} from './funding-components-tis-programmes.model';
import {FundingComponentsTisProgrammesService} from './funding-components-tis-programmes.service';
@Injectable()
export class FundingComponentsTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private fundingComponentsService: FundingComponentsTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.fundingComponentsService.find(id).subscribe((fundingComponents) => {
				this.fundingComponentsModalRef(component, fundingComponents);
			});
		} else {
			return this.fundingComponentsModalRef(component, new FundingComponentsTisProgrammes());
		}
	}

	fundingComponentsModalRef(component: Component, fundingComponents: FundingComponentsTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.fundingComponents = fundingComponents;
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
