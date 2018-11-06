import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs/Rx';
import {WebsocketService} from './websocket.service';

const CHAT_URL = 'ws://localhost:8080/player';

export interface Response {
  data: string;
}

export interface Game {
  id: number;
  width: number;
  height: number;
  gameStatus: string;
  tickCount: number;
  player1: object;
  player2: object;
  playingField: number[][];
  winner: object;
}

@Injectable()
export class GameViewService {

  public subject: Subject<Response>;

  constructor(wsService: WebsocketService) {
    this.subject = <Subject<Response>>wsService
      .connect(CHAT_URL)
      .map((response: MessageEvent): Response => {
        console.log(response.data);
        return { data: response.data}; //JSON.parse(response.data);
      });
  }
}
