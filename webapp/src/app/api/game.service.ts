import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

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

  createGame(playerId){
    return  this.httpClient.post(`${this.API_URL}/singleplayer/" + playerId/`, null);
  }
}
