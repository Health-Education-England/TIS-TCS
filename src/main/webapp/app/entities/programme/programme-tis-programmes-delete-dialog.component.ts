import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {ProgrammeTisProgrammes} from "./programme-tis-programmes.model";
import {ProgrammeTisProgrammesPopupService} from "./programme-tis-programmes-popup.service";
import {ProgrammeTisProgrammesService} from "./programme-tis-programmes.service";

@Component({
	selector: 'jhi-programme-tis-programmes-delete-dialog',
	templateUrl: './programme-tis-programmes-delete-dialog.component.html'
})
export class ProgrammeTisProgrammesDeleteDialogComponent {

	programme: ProgrammeTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeService: ProgrammeTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['programme', 'status']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.programmeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'programmeListModification',
				content: 'Deleted an programme'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-programme-tis-programmes-delete-popup',
	template: ''
})
export class ProgrammeTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private programmePopupService: ProgrammeTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.programmePopupService
				.open(ProgrammeTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
