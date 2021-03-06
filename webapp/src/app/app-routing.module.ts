import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainMenuComponent} from "./main-menu/main-menu.component";
import {NewGameComponent} from "./new-game/new-game.component";
import {SpectateGameComponent} from "./spectate-game/spectate-game.component";
import {JoinGameComponent} from "./join-game/join-game.component";

const routes: Routes = [
  { path: '', component: MainMenuComponent },
  { path: 'newgame', component: NewGameComponent },
  { path: 'spectate', component: SpectateGameComponent },
  { path: 'join', component: JoinGameComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
