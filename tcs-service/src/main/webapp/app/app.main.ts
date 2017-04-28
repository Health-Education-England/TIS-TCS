import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {ProdConfig} from "./blocks/config/prod.config";
import {TcsAppModule} from "./app.module";

ProdConfig();

if (module['hot']) {
	module['hot'].accept();
}

platformBrowserDynamic().bootstrapModule(TcsAppModule);
