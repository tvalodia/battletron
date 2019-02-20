import {NewGamePlayer} from "./new-game-player";

export class NewGame {

  sessionId: string;
  playerOne: NewGamePlayer = new NewGamePlayer();
  playerTwo: NewGamePlayer = new NewGamePlayer();

}
