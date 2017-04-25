import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {navbarRoute} from "../app.route";
import {errorRoute} from "./";

let LAYOUT_ROUTES = [
	navbarRoute,
	...errorRoute
];

const CHILD_ROUTES = [
	// { path: '...', loadChildren: '../tis-components/tis-.../tis-....module#Tis...Module' },
];

@NgModule({
	imports: [
		RouterModule.forRoot(LAYOUT_ROUTES, {useHash: true}),
		RouterModule.forChild(CHILD_ROUTES)

	],
	exports: [
		RouterModule
	]
})
export class LayoutRoutingModule {
}
