import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs/Rx';
import {WebsocketService} from './websocket.service';

const CHAT_URL = 'ws://localhost:8080/player';

export interface Game {
  id: number;
  width: number;
  height: number;
  gameStatus: String;
  tickCount: number;
  player1: object;
  player2: object;
  playingField: number[][];
  winner: object;
}

@Injectable()
export class GameViewService {

  public games: Subject<Game>;

  constructor(wsService: WebsocketService) {
    this.games = <Subject<Game>>wsService
      .connect(CHAT_URL)
      .map((response: MessageEvent): Game => {
        console.log(response.data);
        return {
          id: 1234,
          width: 100,
          height: 100,
          gameStatus: "started",
          tickCount: 1,
          player1: null,
          player2: null,
          playingField: [[0]],
          winner: null
        }; //JSON.parse(response.data);
      });
  }
}
