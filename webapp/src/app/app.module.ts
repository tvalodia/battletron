import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { BattletronMaterialModule } from './material';
import { NewGameComponent } from './new-game/new-game.component';
import { GameViewComponent } from './game-view/game-view.component';

@NgModule({
  declarations: [
    AppComponent,
    MainMenuComponent,
    NewGameComponent,
    GameViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    BattletronMaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
