import {MatButtonModule, MatCardModule} from "@angular/material";
import {NgModule} from "@angular/core";

@NgModule({
  imports: [MatCardModule, MatButtonModule],
  exports: [MatCardModule, MatButtonModule]
})
export class BattletronMaterialModule {
}
