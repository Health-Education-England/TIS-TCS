import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {TariffRateTisProgrammes} from './tariff-rate-tis-programmes.model';
import {TariffRateTisProgrammesService} from './tariff-rate-tis-programmes.service';
@Injectable()
export class TariffRateTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private tariffRateService: TariffRateTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.tariffRateService.find(id).subscribe((tariffRate) => {
				this.tariffRateModalRef(component, tariffRate);
			});
		} else {
			return this.tariffRateModalRef(component, new TariffRateTisProgrammes());
		}
	}

	tariffRateModalRef(component: Component, tariffRate: TariffRateTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.tariffRate = tariffRate;
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
