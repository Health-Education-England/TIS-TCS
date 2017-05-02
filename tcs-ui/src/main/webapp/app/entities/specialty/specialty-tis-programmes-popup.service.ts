import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {SpecialtyTisProgrammes} from "./specialty-tis-programmes.model";
import {SpecialtyTisProgrammesService} from "./specialty-tis-programmes.service";
@Injectable()
export class SpecialtyTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private specialtyService: SpecialtyTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.specialtyService.find(id).subscribe(specialty => {
				this.specialtyModalRef(component, specialty);
			});
		} else {
			return this.specialtyModalRef(component, new SpecialtyTisProgrammes());
		}
	}

	specialtyModalRef(component: Component, specialty: SpecialtyTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.specialty = specialty;
		modalRef.result.then(result => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
