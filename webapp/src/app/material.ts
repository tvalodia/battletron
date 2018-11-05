import {
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule, MatGridListModule, MatListModule,
  MatSelectModule
} from "@angular/material";
import {NgModule} from "@angular/core";

@NgModule({
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatSelectModule, MatGridListModule,
            MatListModule],
  exports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatSelectModule, MatGridListModule,
            MatListModule]
})
export class BattletronMaterialModule {
}
