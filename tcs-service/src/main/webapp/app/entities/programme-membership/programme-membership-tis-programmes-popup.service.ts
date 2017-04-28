import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {ProgrammeMembershipTisProgrammes} from "./programme-membership-tis-programmes.model";
import {ProgrammeMembershipTisProgrammesService} from "./programme-membership-tis-programmes.service";
@Injectable()
export class ProgrammeMembershipTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.programmeMembershipService.find(id).subscribe(programmeMembership => {
				if (programmeMembership.curriculumStartDate) {
					programmeMembership.curriculumStartDate = {
						year: programmeMembership.curriculumStartDate.getFullYear(),
						month: programmeMembership.curriculumStartDate.getMonth() + 1,
						day: programmeMembership.curriculumStartDate.getDate()
					};
				}
				if (programmeMembership.curriculumEndDate) {
					programmeMembership.curriculumEndDate = {
						year: programmeMembership.curriculumEndDate.getFullYear(),
						month: programmeMembership.curriculumEndDate.getMonth() + 1,
						day: programmeMembership.curriculumEndDate.getDate()
					};
				}
				if (programmeMembership.programmeStartDate) {
					programmeMembership.programmeStartDate = {
						year: programmeMembership.programmeStartDate.getFullYear(),
						month: programmeMembership.programmeStartDate.getMonth() + 1,
						day: programmeMembership.programmeStartDate.getDate()
					};
				}
				if (programmeMembership.curriculumCompletionDate) {
					programmeMembership.curriculumCompletionDate = {
						year: programmeMembership.curriculumCompletionDate.getFullYear(),
						month: programmeMembership.curriculumCompletionDate.getMonth() + 1,
						day: programmeMembership.curriculumCompletionDate.getDate()
					};
				}
				if (programmeMembership.programmeEndDate) {
					programmeMembership.programmeEndDate = {
						year: programmeMembership.programmeEndDate.getFullYear(),
						month: programmeMembership.programmeEndDate.getMonth() + 1,
						day: programmeMembership.programmeEndDate.getDate()
					};
				}
				this.programmeMembershipModalRef(component, programmeMembership);
			});
		} else {
			return this.programmeMembershipModalRef(component, new ProgrammeMembershipTisProgrammes());
		}
	}

	programmeMembershipModalRef(component: Component, programmeMembership: ProgrammeMembershipTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.programmeMembership = programmeMembership;
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
