import {
  Component,
  ElementRef,
  EventEmitter,
  NgZone, OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {Game, GameViewService} from "./game-view.service";

@Component({
  selector: 'app-game-view',
  templateUrl: './game-view.component.html',
  styleUrls: ['./game-view.component.css']
})
export class GameViewComponent implements OnInit, OnDestroy {

  BLOCK_SIZE: number = 5;
  @ViewChild('gameViewCanvas') canvasRef: ElementRef;
  sessionId: string = '';
  @Output() sessionIdEventEmitter = new EventEmitter<string>();
  private game: Game;

  constructor(private ngZone: NgZone, private gameViewService: GameViewService) {
    gameViewService.subject.subscribe(msg => {
      if (msg.startsWith("id=")) {
        this.sessionId = msg.substr(3);
        this.sessionIdEventEmitter.emit(msg.substr(3));
      } else {
        this.game = JSON.parse(msg);
        this.paint();
      }
    });
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.gameViewService.subject.unsubscribe();
    this.gameViewService.disconnect();
    console.log("ngOnDestroy");
  }

  private paint() {
    // Paint current frame
    let ctx: CanvasRenderingContext2D =
      this.canvasRef.nativeElement.getContext('2d');

    // // Draw background (which also effectively clears any previous drawing)
    ctx.fillStyle = 'rgb(0, 0, 0)';
    // Clear any previous content.
    ctx.fillRect(0, 0, 500, 500);
    ctx.lineWidth = 1;

    //Draw the players' trails
    for (let x = 0; x < this.game.width; x++) {
      for (let y = 0; y < this.game.height; y++) {
        if (this.game.playingField[x][y] == this.game.playerOne.id) {
          this.drawBlock(ctx, this.getScreenX(x), this.getScreenY(this.game.height, y), "blue");
        }
        if (this.game.playingField[x][y] == this.game.playerTwo.id) {
          this.drawBlock(ctx, this.getScreenX(x), this.getScreenY(this.game.height, y), "magenta");
        }
      }
    }

    //player 1's head
    this.drawBlock(ctx, this.getScreenX(this.game.playerOne.positionX),
      this.getScreenY(this.game.height, this.game.playerOne.positionY), "cyan");
    //player 2's head
    this.drawBlock(ctx, this.getScreenX(this.game.playerTwo.positionX),
      this.getScreenY(this.game.height, this.game.playerTwo.positionY), "pink");


  }

  getScreenX(gameX: number) {
    return gameX * this.BLOCK_SIZE;
  }

  getScreenY(playingFieldHeight: number, gameYCoordinate: number) {
    return ((playingFieldHeight - 1) * this.BLOCK_SIZE) - (gameYCoordinate * this.BLOCK_SIZE);
  }

  drawBlock(ctx: CanvasRenderingContext2D, x: number, y: number, color: string) {
    ctx.beginPath();
    ctx.strokeStyle = color;
    ctx.fillStyle = color;
    ctx.fillRect(x, y, this.BLOCK_SIZE, this.BLOCK_SIZE);
    ctx.stroke()
  }

  onKeydown(event) {
    let key = event.which;

    if (key === 87) {
      this.sendMsg("W");
    } else if (key === 83) {
      this.sendMsg("S");
    } else if (key === 65) {
      this.sendMsg("A");
    } else if (key === 68) {
      this.sendMsg("D");
    } else if (key === 38) {
      this.sendMsg("UP");
    } else if (key === 40) {
      this.sendMsg("DOWN");
    } else if (key === 37) {
      this.sendMsg("LEFT");
    } else if (key === 39) {
      this.sendMsg("RIGHT");
    }
  }

  sendMsg(direction: string) {
    this.gameViewService.subject.next(direction);
  }

  public setGame(game: Game) {
    this.game = game;
    this.paint();
  }

}
