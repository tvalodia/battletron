import {Component, OnInit} from '@angular/core';
import {GameViewService} from '../game-view/game-view.service';
import {WebsocketService} from "../game-view/websocket.service";
import {GameService} from "../api/game.service";

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

  playerTypes: PlayerType[] = [
    {value: 'KEYBOARD_WASD_KEYS', viewValue: 'Human - WASD keys'},
    {value: 'KEYWORD_ARROW_KEYS', viewValue: 'Human - Arrow keys'},
    {value: 'AI_SIMPLE', viewValue: 'AI - Simple'},
    {value: 'AI_DOWNLEFT', viewValue: 'AI - Down Left'}
  ];

  playerId: string = '';
  playerOneType: string = 'KEYBOARD_WASD_KEYS';
  playerTwoType: string = 'KEYWORD_ARROW_KEYS';

  constructor(private gameService: GameService) {
  }

  ngOnInit() {}

  onNewPlayerId(playerId: string) {
    this.playerId = playerId;
  }

  public start() {
    this.gameService.createGame(this.playerId, this.playerOneType, this.playerTwoType)
      .subscribe((data: Array<object>) => {
        console.log(data);
      });

  }
}
