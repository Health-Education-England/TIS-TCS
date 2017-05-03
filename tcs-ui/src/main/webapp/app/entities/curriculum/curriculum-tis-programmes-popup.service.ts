import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {CurriculumTisProgrammes} from "./curriculum-tis-programmes.model";
import {CurriculumTisProgrammesService} from "./curriculum-tis-programmes.service";
@Injectable()
export class CurriculumTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private curriculumService: CurriculumTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.curriculumService.find(id).subscribe(curriculum => {
				if (curriculum.start) {
					curriculum.start = {
						year: curriculum.start.getFullYear(),
						month: curriculum.start.getMonth() + 1,
						day: curriculum.start.getDate()
					};
				}
				if (curriculum.end) {
					curriculum.end = {
						year: curriculum.end.getFullYear(),
						month: curriculum.end.getMonth() + 1,
						day: curriculum.end.getDate()
					};
				}
				this.curriculumModalRef(component, curriculum);
			});
		} else {
			return this.curriculumModalRef(component, new CurriculumTisProgrammes());
		}
	}

	curriculumModalRef(component: Component, curriculum: CurriculumTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.curriculum = curriculum;
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
