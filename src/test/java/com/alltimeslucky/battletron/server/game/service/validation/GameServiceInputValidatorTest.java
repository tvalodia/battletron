package com.alltimeslucky.battletron.server.game.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;
import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.game.controller.GameControllerRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameServiceInputValidatorTest {

    private GameServiceInputValidator validator;
    private GameControllerRepository gameControllerRepository;
    private ClientWebSocketRepository clientWebSocketRepository;

    @Mock
    private GameController mockGameController;

    /**
     * Instantiates the tested class and any mocked objects.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameControllerRepository = new GameControllerRepository();
        clientWebSocketRepository = new ClientWebSocketRepository();
        validator = new GameServiceInputValidator(gameControllerRepository, clientWebSocketRepository);
    }

    @Test
    public void testValidateGetGameInputSuccessful() throws Exception {
        gameControllerRepository.add(123L, mockGameController);

        validator.validateGameId(123L);
    }

    @Test
    public void testValidateGetGameInputWithInvalidSessionId() {
        try {
            validator.validateGameId(321L);
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.NOT_FOUND, exception.getCode());
        }
    }

    @Test
    public void testValidateCreateGameInputSuccessful() throws BattletronException {
        clientWebSocketRepository.add("abc", new ClientWebSocket());

        validator.validateCreateGameInput("abc", "KEYBOARD", "KEYBOARD");
    }

    @Test
    public void testValidateCreateGameInputWithEmptySessionId() {
        try {
            validator.validateCreateGameInput("", "KEYBOARD", "KEYBOARD");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateCreateGameInputWithNullPlayerOneControllerType() {
        clientWebSocketRepository.add("abc", new ClientWebSocket());

        try {
            validator.validateCreateGameInput("abc", null, "KEYBOARD");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Player One Type", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateCreateGameInputWithNullPlayerTwoControllerType() {
        clientWebSocketRepository.add("def", new ClientWebSocket());

        try {
            validator.validateCreateGameInput("def", "KEYBOARD", null);
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Player Two Type", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateCreateGameInputWithNullInputs() {
        clientWebSocketRepository.add("abc", new ClientWebSocket());

        try {
            validator.validateCreateGameInput(null, null, null);
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(3, exception.getExceptions().size());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(1).getCode());
            assertEquals("Player One Type", exception.getExceptions().get(1).getField());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(2).getCode());
            assertEquals("Player Two Type", exception.getExceptions().get(2).getField());
        }
    }

    @Test
    public void testValidateCreateGameInputWithInvalidSessionId() {
        try {
            validator.validateCreateGameInput("abc", "KEYBOARD", "KEYBOARD");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.INVALID_VALUE, exception.getExceptions().get(0).getCode());
        }
    }

    @Test
    public void testValidateCreateGameInputWithAllInvalidValues() {
        try {
            validator.validateCreateGameInput("abc", "def", "ghi");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(3, exception.getExceptions().size());
            assertEquals(ExceptionCode.INVALID_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
            assertEquals(ExceptionCode.INVALID_VALUE, exception.getExceptions().get(1).getCode());
            assertEquals("Player One Type", exception.getExceptions().get(1).getField());
            assertEquals(ExceptionCode.INVALID_VALUE, exception.getExceptions().get(2).getCode());
            assertEquals("Player Two Type", exception.getExceptions().get(2).getField());
        }
    }

    @Test
    public void testValidateSpectateGameInputSuccessful() throws BattletronException {
        gameControllerRepository.add(123L, mockGameController);
        clientWebSocketRepository.add("abc", new ClientWebSocket());

        validator.validateSpectateGameInput(123, "abc");
    }

    @Test
    public void testValidateSpectateGameInputWithInvalidGameId() {

        try {
            validator.validateSpectateGameInput(123, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.NOT_FOUND, exception.getCode());
        }
    }

    @Test
    public void testValidateSpectateGameInputWithNullSessionId() {
        gameControllerRepository.add(123L, mockGameController);

        try {
            validator.validateSpectateGameInput(123, null);
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateSpectateGameInputWithInvalidSessionId() {
        gameControllerRepository.add(123L, mockGameController);

        try {
            validator.validateSpectateGameInput(123, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.INVALID_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateJoinGameInputSuccessful() throws BattletronException {
        gameControllerRepository.add(123L, mockGameController);
        clientWebSocketRepository.add("abc", new ClientWebSocket());

        validator.validateJoinGameInput(123, "abc");
    }

    @Test
    public void testValidateJoinGameInputWithInvalidGameId() {

        try {
            validator.validateJoinGameInput(123, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.NOT_FOUND, exception.getCode());
        }
    }

    @Test
    public void testValidateJoinGameInputWithNullSessionId() {
        gameControllerRepository.add(123L, mockGameController);

        try {
            validator.validateJoinGameInput(123, null);
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.MISSING_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateJoinGameInputWithInvalidSessionId() {
        gameControllerRepository.add(123L, mockGameController);

        try {
            validator.validateJoinGameInput(123, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.VALIDATION, exception.getCode());
            assertEquals(1, exception.getExceptions().size());
            assertEquals(ExceptionCode.INVALID_VALUE, exception.getExceptions().get(0).getCode());
            assertEquals("Session ID", exception.getExceptions().get(0).getField());
        }
    }

    @Test
    public void testValidateJoinGameFailsWhenAlreadyJoinedAsPlayerOne() {
        gameControllerRepository.add(123L, mockGameController);
        ClientWebSocket clientWebSocket = new ClientWebSocket();
        clientWebSocketRepository.add("abc", clientWebSocket);
        PlayerController mockPlayerController = mock(PlayerController.class);
        clientWebSocket.setPlayerController(mockPlayerController);
        when(mockGameController.getPlayerOneController()).thenReturn(mockPlayerController);

        try {
            validator.validateJoinGameInput(123L, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.ALREADY_JOINED_GAME, exception.getCode());
        }
    }

    @Test
    public void testValidateJoinGameFailsWhenAlreadyJoinedAsPlayerTwo() {
        gameControllerRepository.add(123L, mockGameController);
        ClientWebSocket clientWebSocket = new ClientWebSocket();
        clientWebSocketRepository.add("abc", clientWebSocket);
        PlayerController mockPlayerController = mock(PlayerController.class);
        clientWebSocket.setPlayerController(mockPlayerController);
        when(mockGameController.getPlayerTwoController()).thenReturn(mockPlayerController);

        try {
            validator.validateJoinGameInput(123L, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.ALREADY_JOINED_GAME, exception.getCode());
        }
    }

    @Test
    public void testValidateJoinGameFailsWhenGameAlreadyFull() {
        gameControllerRepository.add(123L, mockGameController);
        clientWebSocketRepository.add("abc",  new ClientWebSocket());
        PlayerController mockPlayerController = mock(PlayerController.class);
        when(mockGameController.getPlayerOneController()).thenReturn(mockPlayerController);
        when(mockGameController.getPlayerTwoController()).thenReturn(mockPlayerController);

        try {
            validator.validateJoinGameInput(123L, "abc");
            fail("Exception was not thrown");
        } catch (BattletronException exception) {
            assertEquals(ExceptionCode.GAME_ALREADY_FULL, exception.getCode());
        }
    }

}
