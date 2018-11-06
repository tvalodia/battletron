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

  playerId: string = '';
  player1Type: string = '';
  player2Type: string = '';

  constructor(private gameViewService: GameViewService) {
    gameViewService.subject.subscribe(msg => {
      console.log("Response from websocket: " + msg.data);
      if (msg.data.startsWith("id=")) {
        this.playerId = msg.data.substr(3);
      }
    });
  }

  ngOnInit() {}

  sendMsg(direction: string) {
    console.log('new message from client to websocket: ', {data: direction});
    this.gameViewService.subject.next({data: direction});
  }
}
