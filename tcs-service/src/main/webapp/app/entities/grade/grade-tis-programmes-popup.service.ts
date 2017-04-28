import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {GradeTisProgrammes} from "./grade-tis-programmes.model";
import {GradeTisProgrammesService} from "./grade-tis-programmes.service";
@Injectable()
export class GradeTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private gradeService: GradeTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.gradeService.find(id).subscribe(grade => {
				this.gradeModalRef(component, grade);
			});
		} else {
			return this.gradeModalRef(component, new GradeTisProgrammes());
		}
	}

	gradeModalRef(component: Component, grade: GradeTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.grade = grade;
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
