import {Routes} from "@angular/router";
import {configurationRoute, docsRoute, healthRoute, logsRoute, metricsRoute} from "./";
import {UserRouteAccessService} from "../shared";

let ADMIN_ROUTES = [
	configurationRoute,
	docsRoute,
	healthRoute,
	logsRoute,
	metricsRoute
];


export const adminState: Routes = [{
	path: '',
	data: {},
	canActivate: [UserRouteAccessService],
	children: ADMIN_ROUTES
}
];
