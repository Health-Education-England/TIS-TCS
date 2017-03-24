import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {ProgrammeMembershipTisProgrammes} from "./programme-membership-tis-programmes.model";
import {ProgrammeMembershipTisProgrammesPopupService} from "./programme-membership-tis-programmes-popup.service";
import {ProgrammeMembershipTisProgrammesService} from "./programme-membership-tis-programmes.service";

@Component({
	selector: 'jhi-programme-membership-tis-programmes-delete-dialog',
	templateUrl: './programme-membership-tis-programmes-delete-dialog.component.html'
})
export class ProgrammeMembershipTisProgrammesDeleteDialogComponent {

	programmeMembership: ProgrammeMembershipTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['programmeMembership', 'programmeMembershipType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.programmeMembershipService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'programmeMembershipListModification',
				content: 'Deleted an programmeMembership'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-programme-membership-tis-programmes-delete-popup',
	template: ''
})
export class ProgrammeMembershipTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private programmeMembershipPopupService: ProgrammeMembershipTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.programmeMembershipPopupService
				.open(ProgrammeMembershipTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
