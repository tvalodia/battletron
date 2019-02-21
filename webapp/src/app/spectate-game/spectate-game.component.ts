import {Component, OnInit, ViewChild} from '@angular/core';
import {GameService} from "../api/game.service";
import {WebsocketService} from "../game-view/websocket.service";
import {Game, GameViewService} from "../game-view/game-view.service";
import {GameViewComponent} from "../game-view/game-view.component";
import {SpectateGame} from "./spectate-game";

@Component({
  selector: 'app-spectate-game',
  templateUrl: './spectate-game.component.html',
  styleUrls: ['./spectate-game.component.css'],
  providers: [WebsocketService, GameViewService]
})
export class SpectateGameComponent implements OnInit {

  spectateGameData: SpectateGame = new SpectateGame();
  games: Array<Game>;
  @ViewChild('gameView') gameViewRef: GameViewComponent;

  constructor(private gameService: GameService) {
  }

  ngOnInit() {
    this.getGames();
  }

  onSessionId(sessionId: string) {
    this.spectateGameData.sessionId = sessionId;
  }

  public getGames() {
    this.gameService.getGames().subscribe((data: Array<Game>) => {
      this.games = data;
      console.log(data);
    });
  }

  public refresh() {
    this.getGames();
  }

  public spectateGame(gameId: number) {
    this.gameService.spectateGame(gameId, this.spectateGameData).subscribe((data: Game) => {
      this.gameViewRef.setGame(data);
      console.log(gameId);
    });

  }
}
