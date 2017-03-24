import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {CurriculumTisProgrammes} from "./curriculum-tis-programmes.model";
import {CurriculumTisProgrammesPopupService} from "./curriculum-tis-programmes-popup.service";
import {CurriculumTisProgrammesService} from "./curriculum-tis-programmes.service";

@Component({
	selector: 'jhi-curriculum-tis-programmes-delete-dialog',
	templateUrl: './curriculum-tis-programmes-delete-dialog.component.html'
})
export class CurriculumTisProgrammesDeleteDialogComponent {

	curriculum: CurriculumTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private curriculumService: CurriculumTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['curriculum', 'curriculumSubType', 'assessmentType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.curriculumService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'curriculumListModification',
				content: 'Deleted an curriculum'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-curriculum-tis-programmes-delete-popup',
	template: ''
})
export class CurriculumTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private curriculumPopupService: CurriculumTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.curriculumPopupService
				.open(CurriculumTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
