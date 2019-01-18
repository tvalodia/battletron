import {Component, OnInit, ViewChild} from '@angular/core';
import {GameService} from "../api/game.service";
import {WebsocketService} from "../game-view/websocket.service";
import {Game, GameViewService} from "../game-view/game-view.service";
import {GameViewComponent} from "../game-view/game-view.component";

@Component({
  selector: 'app-join-game',
  templateUrl: './join-game.component.html',
  styleUrls: ['./join-game.component.css'],
  providers: [WebsocketService, GameViewService]
})
export class JoinGameComponent implements OnInit {

  playerId: string = '';
  games: Array<Game>;
  @ViewChild('gameView') gameViewRef: GameViewComponent;

  constructor(private gameService: GameService) {
  }

  ngOnInit() {
    this.getGames();
  }

  onNewPlayerId(playerId: string) {
    this.playerId = playerId;
  }

  public getGames() {
    this.gameService.getJoinableGames().subscribe((data: Array<Game>) => {
      this.games = data;
      console.log(data);
    });
  }

  public refresh() {
    this.getGames();
  }

  public joinGame(gameId: number) {
    this.gameService.joinGame(gameId, this.playerId).subscribe((data: Game) => {
      this.gameViewRef.setGame(data);
      console.log(gameId);
    });

  }

}
