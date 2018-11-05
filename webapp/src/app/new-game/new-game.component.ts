import { Component, OnInit } from '@angular/core';

export interface PlayerType {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-new-game',
  templateUrl: './new-game.component.html',
  styleUrls: ['./new-game.component.css']
})
export class NewGameComponent implements OnInit {

  playerTypes: PlayerType[] = [
    {value: 'human', viewValue: 'Human'},
    {value: 'simpleAi', viewValue: 'Simple AI'},
    {value: 'downLeftAi', viewValue: 'Down Left AI'}
  ];

  player1Type: String = '';
  player2Type: String = '';

  constructor() {
  }

  ngOnInit() {
  }

}
