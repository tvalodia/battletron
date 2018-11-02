import {MatButtonModule, MatButtonToggleModule, MatCardModule} from "@angular/material";
import {NgModule} from "@angular/core";

@NgModule({
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule],
  exports: [MatCardModule, MatButtonModule, MatButtonToggleModule]
})
export class BattletronMaterialModule {
}
