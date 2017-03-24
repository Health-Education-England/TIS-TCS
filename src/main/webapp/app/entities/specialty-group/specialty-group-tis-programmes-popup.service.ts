import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {SpecialtyGroupTisProgrammes} from "./specialty-group-tis-programmes.model";
import {SpecialtyGroupTisProgrammesService} from "./specialty-group-tis-programmes.service";
@Injectable()
export class SpecialtyGroupTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private specialtyGroupService: SpecialtyGroupTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.specialtyGroupService.find(id).subscribe(specialtyGroup => {
				this.specialtyGroupModalRef(component, specialtyGroup);
			});
		} else {
			return this.specialtyGroupModalRef(component, new SpecialtyGroupTisProgrammes());
		}
	}

	specialtyGroupModalRef(component: Component, specialtyGroup: SpecialtyGroupTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.specialtyGroup = specialtyGroup;
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
