import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {DatePipe} from "@angular/common";
import {CookieService} from "angular2-cookie/services/cookies.service";
import {
	TcsSharedLibsModule,
	TcsSharedCommonModule,
	CSRFService,
	AuthService,
	AuthServerProvider,
	AccountService,
	UserService,
	StateStorageService,
	LoginService,
	LoginModalService,
	Principal,
	HasAnyAuthorityDirective,
	JhiLoginModalComponent
} from "./";

@NgModule({
	imports: [
		TcsSharedLibsModule,
		TcsSharedCommonModule
	],
	declarations: [
		JhiLoginModalComponent,
		HasAnyAuthorityDirective
	],
	providers: [
		CookieService,
		LoginService,
		LoginModalService,
		AccountService,
		StateStorageService,
		Principal,
		CSRFService,
		AuthServerProvider,
		AuthService,
		UserService,
		DatePipe
	],
	entryComponents: [JhiLoginModalComponent],
	exports: [
		TcsSharedCommonModule,
		JhiLoginModalComponent,
		HasAnyAuthorityDirective,
		DatePipe
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class TcsSharedModule {
}
