import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Rx';
import {WebsocketService} from './websocket.service';

const GAME_URL = 'ws://' + document.location.host + '/player';

export interface Game {
  id: number;
  width: number;
  height: number;
  gameStatus: string;
  tickCount: number;
  playerOne: Player;
  playerTwo: Player;
  playingField: number[][];
  winner: Player;
}

export interface Player {
  id: number;
  positionX: number;
  positionY: number;
}

@Injectable()
export class GameViewService {

  public subject: Subject<string>;

  constructor(public wsService: WebsocketService) {
    console.log(document.location);

    this.subject = <Subject<string>>wsService
      .connect(GAME_URL)
      .map((response: MessageEvent): string => {
        return response.data; //JSON.parse(response.data);
      });
  }

  public disconnect() {
    this.wsService.disconnect();
  }
}
