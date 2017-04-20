import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {TariffFundingTypeFieldsTisProgrammes} from './tariff-funding-type-fields-tis-programmes.model';
import {TariffFundingTypeFieldsTisProgrammesService} from './tariff-funding-type-fields-tis-programmes.service';
@Injectable()
export class TariffFundingTypeFieldsTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private tariffFundingTypeFieldsService: TariffFundingTypeFieldsTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.tariffFundingTypeFieldsService.find(id).subscribe((tariffFundingTypeFields) => {
				if (tariffFundingTypeFields.effectiveDateFrom) {
					tariffFundingTypeFields.effectiveDateFrom = {
						year: tariffFundingTypeFields.effectiveDateFrom.getFullYear(),
						month: tariffFundingTypeFields.effectiveDateFrom.getMonth() + 1,
						day: tariffFundingTypeFields.effectiveDateFrom.getDate()
					};
				}
				if (tariffFundingTypeFields.effectiveDateTo) {
					tariffFundingTypeFields.effectiveDateTo = {
						year: tariffFundingTypeFields.effectiveDateTo.getFullYear(),
						month: tariffFundingTypeFields.effectiveDateTo.getMonth() + 1,
						day: tariffFundingTypeFields.effectiveDateTo.getDate()
					};
				}
				this.tariffFundingTypeFieldsModalRef(component, tariffFundingTypeFields);
			});
		} else {
			return this.tariffFundingTypeFieldsModalRef(component, new TariffFundingTypeFieldsTisProgrammes());
		}
	}

	tariffFundingTypeFieldsModalRef(component: Component, tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.tariffFundingTypeFields = tariffFundingTypeFields;
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
