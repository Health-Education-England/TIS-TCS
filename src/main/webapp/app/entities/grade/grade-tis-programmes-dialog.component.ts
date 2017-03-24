import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {GradeTisProgrammes} from "./grade-tis-programmes.model";
import {GradeTisProgrammesPopupService} from "./grade-tis-programmes-popup.service";
import {GradeTisProgrammesService} from "./grade-tis-programmes.service";
import {CurriculumTisProgrammes, CurriculumTisProgrammesService} from "../curriculum";
@Component({
	selector: 'jhi-grade-tis-programmes-dialog',
	templateUrl: './grade-tis-programmes-dialog.component.html'
})
export class GradeTisProgrammesDialogComponent implements OnInit {

	grade: GradeTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	curricula: CurriculumTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private gradeService: GradeTisProgrammesService,
				private curriculumService: CurriculumTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['grade']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.curriculumService.query().subscribe(
			(res: Response) => {
				this.curricula = res.json();
			}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.grade.id !== undefined) {
			this.gradeService.update(this.grade)
				.subscribe((res: GradeTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.gradeService.create(this.grade)
				.subscribe((res: GradeTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: GradeTisProgrammes) {
		this.eventManager.broadcast({name: 'gradeListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}

	trackCurriculumById(index: number, item: CurriculumTisProgrammes) {
		return item.id;
	}

	getSelected(selectedVals: Array<any>, option: any) {
		if (selectedVals) {
			for (let i = 0; i < selectedVals.length; i++) {
				if (option.id === selectedVals[i].id) {
					return selectedVals[i];
				}
			}
		}
		return option;
	}
}

@Component({
	selector: 'jhi-grade-tis-programmes-popup',
	template: ''
})
export class GradeTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private gradePopupService: GradeTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.gradePopupService
					.open(GradeTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.gradePopupService
					.open(GradeTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
