import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {SpecialtyGroupTisProgrammes} from "./specialty-group-tis-programmes.model";
import {SpecialtyGroupTisProgrammesPopupService} from "./specialty-group-tis-programmes-popup.service";
import {SpecialtyGroupTisProgrammesService} from "./specialty-group-tis-programmes.service";
@Component({
	selector: 'jhi-specialty-group-tis-programmes-dialog',
	templateUrl: './specialty-group-tis-programmes-dialog.component.html'
})
export class SpecialtyGroupTisProgrammesDialogComponent implements OnInit {

	specialtyGroup: SpecialtyGroupTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private specialtyGroupService: SpecialtyGroupTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['specialtyGroup']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.specialtyGroup.id !== undefined) {
			this.specialtyGroupService.update(this.specialtyGroup)
				.subscribe((res: SpecialtyGroupTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.specialtyGroupService.create(this.specialtyGroup)
				.subscribe((res: SpecialtyGroupTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: SpecialtyGroupTisProgrammes) {
		this.eventManager.broadcast({name: 'specialtyGroupListModification', content: 'OK'});
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
}

@Component({
	selector: 'jhi-specialty-group-tis-programmes-popup',
	template: ''
})
export class SpecialtyGroupTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private specialtyGroupPopupService: SpecialtyGroupTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.specialtyGroupPopupService
					.open(SpecialtyGroupTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.specialtyGroupPopupService
					.open(SpecialtyGroupTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
