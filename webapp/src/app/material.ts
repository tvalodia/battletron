import {
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule, MatGridListModule, MatListModule,
  MatSelectModule, MatDialogModule
} from "@angular/material";
import {NgModule} from "@angular/core";

@NgModule({
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatSelectModule, MatGridListModule,
            MatListModule, MatDialogModule],
  exports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatSelectModule, MatGridListModule,
            MatListModule, MatDialogModule]
})
export class BattletronMaterialModule {
}
