import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {TrainingNumberTisProgrammes} from "./training-number-tis-programmes.model";
import {TrainingNumberTisProgrammesService} from "./training-number-tis-programmes.service";
@Injectable()
export class TrainingNumberTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private trainingNumberService: TrainingNumberTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.trainingNumberService.find(id).subscribe(trainingNumber => {
				this.trainingNumberModalRef(component, trainingNumber);
			});
		} else {
			return this.trainingNumberModalRef(component, new TrainingNumberTisProgrammes());
		}
	}

	trainingNumberModalRef(component: Component, trainingNumber: TrainingNumberTisProgrammes): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.trainingNumber = trainingNumber;
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
