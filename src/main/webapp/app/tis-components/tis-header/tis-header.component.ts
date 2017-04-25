import { Component, OnInit } from '@angular/core';
import {JhiLanguageService} from "ng-jhipster";

@Component({
	moduleId: module.id.toString(),
	selector: 'jhi-tis-header',
	templateUrl: './tis-header.component.html'
})

export class TisHeaderComponent implements OnInit {

	username = "";
	isCollapsed = true;

	constructor(
		private jhiLanguageService: JhiLanguageService
	) {
		this.jhiLanguageService.setLocations(['tis-header']);
	}

	ngOnInit() {
		// Set Users Name;
		this.username = "Firstname Lastname";
	}
}
