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
import com.alltimeslucky.battletron.server.session.Session;
import com.alltimeslucky.battletron.server.session.SessionRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameServiceInputValidatorTest {

    private GameServiceInputValidator validator;
    private GameControllerRepository gameControllerRepository;
    private SessionRepository sessionRepository;

    @Mock
    private GameController mockGameController;

    /**
     * Instantiates the tested class and any mocked objects.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameControllerRepository = new GameControllerRepository();
        sessionRepository = new SessionRepository();
        validator = new GameServiceInputValidator(gameControllerRepository, sessionRepository);
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
        sessionRepository.add("abc", new Session("abc"));

        validator.validateCreateGameInput("abc", "KEYBOARD", null,"KEYBOARD", null);
    }

    @Test
    public void testValidateCreateGameInputWithEmptySessionId() {
        try {
            validator.validateCreateGameInput("", "KEYBOARD", null, "KEYBOARD", null);
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
        sessionRepository.add("abc", new Session("abc"));

        try {
            validator.validateCreateGameInput("abc", null, null, "KEYBOARD", null);
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
        sessionRepository.add("def", new Session("abc"));

        try {
            validator.validateCreateGameInput("def", "KEYBOARD", null, null, null);
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
        sessionRepository.add("abc", new Session("abc"));

        try {
            validator.validateCreateGameInput(null, null, null, null, null);
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
            validator.validateCreateGameInput("abc", "KEYBOARD", null, "KEYBOARD", null);
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
            validator.validateCreateGameInput("abc", "def", null,"ghi", null);
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
        sessionRepository.add("abc", new Session("abc"));

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
        sessionRepository.add("abc", new Session("abc"));

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
        Session session = new Session("abc");
        sessionRepository.add("abc", session);
        PlayerController mockPlayerController = mock(PlayerController.class);
        session.setPlayerController(mockPlayerController);
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
        Session session = new Session("abc");
        sessionRepository.add("abc", session);
        PlayerController mockPlayerController = mock(PlayerController.class);
        session.setPlayerController(mockPlayerController);
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
        sessionRepository.add("abc",   new Session("abc"));
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

    @Test
    public void testValidateCreateGameWithPlayerOneRemoteAiSuccessful() throws BattletronException {
        sessionRepository.add("abc", new Session("abc"));
        validator.validateCreateGameInput("abc", "AI_REMOTE", "http://localhost:5000", "KEYBOARD", null);
    }

    @Test
    public void testValidateCreateGameWithPlayerTwoRemoteAiSuccessful() throws BattletronException {
        sessionRepository.add("abc", new Session("abc"));
        validator.validateCreateGameInput("abc", "KEYBOARD", null,"AI_REMOTE", "http://localhost:5000");
    }

}
