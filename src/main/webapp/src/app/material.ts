import {MatButtonModule, MatCardModule, MatCheckboxModule} from "@angular/material";
import {NgModule} from "@angular/core";

@NgModule({
  imports: [MatCardModule, MatButtonModule, MatCheckboxModule],
  exports: [MatCardModule, MatButtonModule, MatCheckboxModule]
})
export class BattletronMaterialModule {
}
