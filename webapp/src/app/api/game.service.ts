import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


export interface NewGame {
  playerId: string,
  playerOneType: string,
  playerTwoType: string
}


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

  createGame(playerId: string,playerOneType: string, playerTwoType: string){
    return this.httpClient.post(this.API_URL, { playerId: playerId, playerOneType: playerOneType, playerTwoType: playerTwoType});
  }

  joinGame(gameId: number, playerId: string){
    return this.httpClient.post(this.API_URL + "/" + gameId + "/join" , { playerId: playerId});
  }
}
