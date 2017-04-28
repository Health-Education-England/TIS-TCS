import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {ProgrammeTisProgrammes} from "./programme-tis-programmes.model";
import {ProgrammeTisProgrammesService} from "./programme-tis-programmes.service";
@Injectable()
export class ProgrammeTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private programmeService: ProgrammeTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.programmeService.find(id).subscribe(programme => {
				this.programmeModalRef(component, programme);
			});
		} else {
			return this.programmeModalRef(component, new ProgrammeTisProgrammes());
		}
	}

	programmeModalRef(component: Component, programme: ProgrammeTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.programme = programme;
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
