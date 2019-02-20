import {Component, OnInit} from '@angular/core';
import {GameViewService} from '../game-view/game-view.service';
import {WebsocketService} from "../game-view/websocket.service";
import {GameService} from "../api/game.service";
import {NewGame} from "./new-game";

export interface PlayerType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-new-game',
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css'],
  providers: [WebsocketService, GameViewService]
})
export class NewGameComponent implements OnInit {

  newGame: NewGame = new NewGame();

  playerTypes: PlayerType[] = [
    {value: 'KEYBOARD_WASD_KEYS', viewValue: 'Human - WASD keys'},
    {value: 'KEYWORD_ARROW_KEYS', viewValue: 'Human - Arrow keys'},
    {value: 'AI_SIMPLE', viewValue: 'AI - Simple'},
    {value: 'AI_DOWNLEFT', viewValue: 'AI - Down Left'},
    {value: 'OPEN', viewValue: 'Open'}
  ];

  constructor(private gameService: GameService) {
    this.newGame.playerOne.playerType = this.playerTypes[0].value;
    this.newGame.playerTwo.playerType = this.playerTypes[0].value;
  }

  ngOnInit() {}

  onNewPlayerId(sessionId: string) {
    this.newGame.sessionId = sessionId;
  }

  public start() {
    this.gameService.createGame(this.newGame)
      .subscribe((data: Array<object>) => {
        console.log(data);
      });

  }
}
