import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NewGame} from "../new-game/new-game";
import {JoinGame} from "../join-game/join-game";
import {SpectateGame} from "../spectate-game/spectate-game";


@Injectable({
  providedIn: 'root'
})
export class GameService {

  API_URL: string = '/api/game';

  constructor(private  httpClient: HttpClient) {
  }

  getGames() {
    return this.httpClient.get(this.API_URL);
  }

  getJoinableGames() {
    return this.httpClient.get(this.API_URL + "/open");
  }

  spectateGame(gameId: number, spectateGame: SpectateGame) {
    return this.httpClient.post(this.API_URL + "/" + gameId + "/spectate", JSON.stringify(spectateGame));
  }

  createGame(newGame: NewGame) {
    return this.httpClient.post(this.API_URL, JSON.stringify(newGame));
  }

  joinGame(gameId: number, joinGame: JoinGame) {
    return this.httpClient.post(this.API_URL + "/" + gameId + "/join", JSON.stringify(joinGame));
  }
}
