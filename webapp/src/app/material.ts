import {
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule, MatGridListModule, MatListModule,
  MatSelectModule, MatDialogModule, MatInputModule
} from "@angular/material";
import {NgModule} from "@angular/core";

@NgModule({
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatSelectModule, MatGridListModule,
            MatListModule, MatDialogModule, MatInputModule],
  exports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatSelectModule, MatGridListModule,
            MatListModule, MatDialogModule, MatInputModule]
})
export class BattletronMaterialModule {
}
