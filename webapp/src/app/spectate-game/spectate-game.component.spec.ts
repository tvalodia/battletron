import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpectateGameComponent } from './spectate-game.component';

describe('SpectateGameComponent', () => {
  let component: SpectateGameComponent;
  let fixture: ComponentFixture<SpectateGameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpectateGameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpectateGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
