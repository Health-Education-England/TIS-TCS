import { NgModule } from '@angular/core';
// we need to import jhipster shared module to use translations, etc.
import {TcsSharedModule} from "../../shared";
import {TisHeaderComponent } from './tis-header.component';

@NgModule({
	imports: [
		TcsSharedModule
	],
	exports: [TisHeaderComponent],
	declarations: [TisHeaderComponent],
	providers: []
})
export class TisHeaderModule { }
