import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


export interface NewGame {
  playerId: string,
  player1Type: string,
  player2Type: string
}


@Injectable({
  providedIn: 'root'
})
export class GameService {

  API_URL: string  =  'http://localhost:8080/api/game';

  constructor(private  httpClient: HttpClient) {
  }

  getGames(){
    return  this.httpClient.get(this.API_URL);
  }

  createGame(playerId: string,player1Type: string, player2Type: string){
    return this.httpClient.post(this.API_URL, { playerId: playerId, player1Type: player1Type, player2Type: player2Type});
  }
}
