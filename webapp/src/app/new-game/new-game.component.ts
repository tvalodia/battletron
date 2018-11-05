import { Component, OnInit } from '@angular/core';
import {GameViewService} from '../game-view/game-view.service';
import {WebsocketService} from "../game-view/websocket.service";

export interface PlayerType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-new-game',
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css'],
  providers: [ WebsocketService, GameViewService ]
})
export class NewGameComponent implements OnInit {

  playerTypes: PlayerType[] = [
    {value: 'human', viewValue: 'Human'},
    {value: 'simpleAi', viewValue: 'Simple AI'},
    {value: 'downLeftAi', viewValue: 'Down Left AI'}
  ];

  player1Type: String = '';
  player2Type: String = '';

  constructor(private gameViewService: GameViewService) {
    gameViewService.games.subscribe(msg => {
      console.log("Response from websocket: " + msg);
    });
  }

  ngOnInit() {
  }

}
