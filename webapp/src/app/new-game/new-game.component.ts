import {Component, OnInit} from '@angular/core';
import {GameViewService} from '../game-view/game-view.service';
import {WebsocketService} from "../game-view/websocket.service";
import {GameService} from "../api/game.service";
import {NewGame} from "./new-game";
import {FormBuilder, FormGroup, NgForm} from "@angular/forms";

export interface PlayerType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-new-game',
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css'],
  providers: [WebsocketService, GameViewService]
})
export class NewGameComponent implements OnInit {

  newGameForm: FormGroup;
  sessionId: string;
  playerTypes: PlayerType[] = [
    {value: 'KEYBOARD_WASD_KEYS', viewValue: 'Human - WASD keys'},
    {value: 'KEYWORD_ARROW_KEYS', viewValue: 'Human - Arrow keys'},
    {value: 'AI_SIMPLE', viewValue: 'AI - Simple'},
    {value: 'AI_DOWNLEFT', viewValue: 'AI - Down Left'},
    {value: 'AI_REMOTE', viewValue: 'AI - Remote'},
    {value: 'OPEN', viewValue: 'Open'}
  ];

  constructor(private formBuilder: FormBuilder, private gameService: GameService) {
    this.newGameForm = formBuilder.group( {
      playerOneType: [this.playerTypes[0].value],
      playerTwoType: [this.playerTypes[0].value],
      playerOneAiRemoteHost: ["http://localhost:5000"],
      playerTwoAiRemoteHost: ["http://localhost:5000"]
    });
  }

  ngOnInit() {}

  onNewPlayerId(sessionId: string) {
    this.sessionId = sessionId;
  }

  public start(newGameData: NewGame) {
    this.gameService.createGame(newGameData)
      .subscribe((data: Array<object>) => {
        console.log(data);
      });

  }

  showPlayerOneHost() {
    return this.newGameForm.controls.playerOneType.value === "AI_REMOTE";
  }

  showPlayerTwoHost() {
    return this.newGameForm.controls.playerTwoType.value === "AI_REMOTE";
  }

  showHostRow() {
    return this.showPlayerOneHost() || this.showPlayerTwoHost();
  }

  onFormSubmit(form:NgForm) {
    let newGameData: NewGame = new NewGame();
    newGameData.sessionId = this.sessionId;
    newGameData.playerOne.playerType = form["playerOneType"];

    if (this.showPlayerOneHost()) {
      newGameData.playerOne.aiRemoteHost = form["playerOneAiRemoteHost"];
    }

    newGameData.playerTwo.playerType = form["playerTwoType"];
    if (this.showPlayerTwoHost()) {
      newGameData.playerTwo.aiRemoteHost = form["playerTwoAiRemoteHost"];
    }

    this.start(newGameData );

  }


}
