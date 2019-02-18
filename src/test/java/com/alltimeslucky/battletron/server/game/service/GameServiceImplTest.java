package com.alltimeslucky.battletron.server.game.service;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.controller.GameControllerRepository;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.game.model.GameStatus;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;
import com.alltimeslucky.battletron.player.model.PlayerFactory;
import com.alltimeslucky.battletron.server.game.service.validation.GameServiceInputValidator;
import com.alltimeslucky.battletron.server.session.Session;
import com.alltimeslucky.battletron.server.session.SessionRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketController;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameServiceImplTest {

    private static final String PLAYER_ID = "PLAYER_ID";
    private static final String PLAYER_CONTROLLER_TYPE_OPEN = PlayerControllerType.OPEN.toString();

    private GameService gameService;
    private GameControllerRepository gameControllerRepository;
    private GameControllerFactory gameControllerFactory;

    private Session session;

        @Mock
    PlayerControllerFactory mockPlayerControllerFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SessionRepository  sessionRepository = new SessionRepository();
        ClientWebSocketController mockClientWebSocketController = new ClientWebSocketController(sessionRepository);
        gameControllerFactory = new GameControllerFactory();
        PlayerController mockPlayerController1 = mock(PlayerController.class);
        when(mockPlayerControllerFactory.getPlayerController(any(), any(), any())).thenReturn(mockPlayerController1).thenReturn(null);
        gameControllerRepository = new GameControllerRepository();
        GameFactory gameFactory = new GameFactory(new PlayerFactory());
        GameServiceInputValidator gameServiceInputValidator = new GameServiceInputValidator(gameControllerRepository, sessionRepository);
        session = new Session(PLAYER_ID);
        session.setGameId(123L);
        ClientWebSocket mockClientWebSocket = mock(ClientWebSocket.class);
        session.setClientWebSocket(mockClientWebSocket);
        sessionRepository.add(PLAYER_ID, session);

        gameService = new GameServiceImpl(gameControllerRepository, sessionRepository, mockClientWebSocketController,
                mockPlayerControllerFactory, gameControllerFactory, gameFactory, gameServiceInputValidator);
    }


    @Test
    public void testCreateSuccessful() throws BattletronException {

        Game game = gameService.createGame(PLAYER_ID, PLAYER_CONTROLLER_TYPE_OPEN, PLAYER_CONTROLLER_TYPE_OPEN);

        assertTrue(gameControllerRepository.contains(game.getId()));
        assertEquals(GameStatus.WAITING_FOR_READY, game.getGameStatus());
        assertTrue(gameControllerRepository.get(game.getId()).isAlive());

    }

    @Test
    public void testJoinSuccessful() throws BattletronException {
        Game game = gameService.createGame(PLAYER_ID, PLAYER_CONTROLLER_TYPE_OPEN, PLAYER_CONTROLLER_TYPE_OPEN);
        PlayerController mockPlayerController2 = mock(PlayerController.class);
        when(mockPlayerControllerFactory.getPlayerController(PlayerControllerType.KEYBOARD, session.getClientWebSocket(), game.getPlayerTwo())).thenReturn(mockPlayerController2);

        Game joinedGame = gameService.joinGame(game.getId(), PLAYER_ID);

        GameController gameController = gameControllerRepository.get(joinedGame.getId());
        assertNotNull(gameController.getPlayerTwoController());
        assertSame(gameController.getPlayerTwoController(), mockPlayerController2);
        assertEquals(GameStatus.WAITING_FOR_READY, game.getGameStatus());

    }

    @Test(expected = BattletronException.class)
    public void testJoinGameFailsWhenJoiningTheSameGameTwice() throws BattletronException {
        Game game = gameService.createGame(PLAYER_ID, PLAYER_CONTROLLER_TYPE_OPEN, PLAYER_CONTROLLER_TYPE_OPEN);
        PlayerController mockPlayerController2 = mock(PlayerController.class);
        when(mockPlayerControllerFactory.getPlayerController(PlayerControllerType.KEYBOARD, session.getClientWebSocket(), game.getPlayerTwo())).thenReturn(mockPlayerController2);
        session.setPlayerController(mockPlayerController2);

        gameService.joinGame(game.getId(), PLAYER_ID);
        gameService.joinGame(game.getId(), PLAYER_ID);
    }

    @Test
    public void testJoinDifferentGamesSuccessful() throws BattletronException {
        PlayerController mockPlayerController2 = mock(PlayerController.class);
        when(mockPlayerControllerFactory.getPlayerController(any(), any(), any())).thenReturn(null).thenReturn(null)
                .thenReturn(null).thenReturn(null)
                .thenReturn(mockPlayerController2);
        Game game1 = gameService.createGame(PLAYER_ID, PLAYER_CONTROLLER_TYPE_OPEN, PLAYER_CONTROLLER_TYPE_OPEN);
        Game game2 = gameService.createGame(PLAYER_ID, PLAYER_CONTROLLER_TYPE_OPEN, PLAYER_CONTROLLER_TYPE_OPEN);


        gameService.joinGame(game1.getId(), PLAYER_ID);
        gameService.joinGame(game2.getId(), PLAYER_ID);
        gameService.joinGame(game1.getId(), PLAYER_ID);
    }

    @After
    public void tearDown() {
        for (GameController gameController : gameControllerRepository.getAllGameControllers()) {
            gameController.kill();
        }
    }
}
