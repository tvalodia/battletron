import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NewGame} from "../new-game/new-game";


@Injectable({
  providedIn: 'root'
})
export class GameService {

  API_URL: string  =  'http://localhost:8080/api/game';

  constructor(private  httpClient: HttpClient) {
  }

  getGames(){
    return this.httpClient.get(this.API_URL);
  }

  getJoinableGames(){
    return this.httpClient.get(this.API_URL + "/open");
  }

  spectateGame(gameId: number, playerId: string){
    return this.httpClient.post(this.API_URL + "/" + gameId + "/spectate" , { playerId: playerId});
  }

  createGame(newGame: NewGame){
    return this.httpClient.post(this.API_URL,  JSON.stringify(newGame));
  }

  joinGame(gameId: number, playerId: string){
    return this.httpClient.post(this.API_URL + "/" + gameId + "/join" , { playerId: playerId});
  }
}
