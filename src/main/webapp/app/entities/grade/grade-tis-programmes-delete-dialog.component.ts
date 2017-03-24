import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {GradeTisProgrammes} from "./grade-tis-programmes.model";
import {GradeTisProgrammesPopupService} from "./grade-tis-programmes-popup.service";
import {GradeTisProgrammesService} from "./grade-tis-programmes.service";

@Component({
	selector: 'jhi-grade-tis-programmes-delete-dialog',
	templateUrl: './grade-tis-programmes-delete-dialog.component.html'
})
export class GradeTisProgrammesDeleteDialogComponent {

	grade: GradeTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private gradeService: GradeTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['grade']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.gradeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'gradeListModification',
				content: 'Deleted an grade'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-grade-tis-programmes-delete-popup',
	template: ''
})
export class GradeTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gradePopupService: GradeTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.gradePopupService
				.open(GradeTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
