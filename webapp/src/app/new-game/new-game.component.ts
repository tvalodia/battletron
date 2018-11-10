import {AfterViewInit, Component, NgZone, OnInit, ViewChild} from '@angular/core';
import {GameViewService} from '../game-view/game-view.service';
import {WebsocketService} from "../game-view/websocket.service";
import {GameViewComponent} from "../game-view/game-view.component";
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
    {value: 'w-a-s-d', viewValue: 'Human - WASD keys'},
    {value: 'arrowKeys', viewValue: 'Human - Arrow keys'},
    {value: 'simpleAi', viewValue: 'AI - Simple'},
    {value: 'downLeftAi', viewValue: 'AI - Down Left'}
  ];

  playerId: string = '';
  player1Type: string = '';
  player2Type: string = '';

  constructor(private gameService: GameService) {
  }

  ngOnInit() {
    this.getGames();
  }

  onNewPlayerId(playerId: string) {
    this.playerId = playerId;
  }

  public getGames() {
    this.gameService.getGames().subscribe((data: Array<object>) => {
      console.log(data);
    });
  }

  public start() {
    this.gameService.createGame(this.playerId, this.player1Type, this.player2Type)
      .subscribe((data: Array<object>) => {
        console.log(data);
      });

  }
}
