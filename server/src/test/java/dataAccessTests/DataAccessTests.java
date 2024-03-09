package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;
import service.DBService;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DataAccessTests {

    private UserService userService = new UserService();
    private DBService dbService = new DBService();
    private GameService gameService = new GameService();

    private UserDAO userDAO = new SQLUserDAO();
    private AuthDAO authDAO = new SQLAuthDAO();
    private GameDAO gameDAO = new SQLGameDAO();

    @BeforeEach
    public void setup() {
        userDAO.clearUserList();
        authDAO.clearAuthList();
        gameDAO.clearGameList();
    }

    @Test
    public void goodRegRespond() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse testResp = userService.regRespond(req, userDAO, authDAO);

        assertEquals(authDAO.getAuthByID(0).username(), req.username);
        assertEquals(userDAO.getUser(0).username(), req.username);
    }

    @Test
    public void badRegRespond() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterRequest badReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse testResp = userService.regRespond(badReq, userDAO, authDAO);

        assertNotEquals(authDAO.getAuthByID(0).username(), null);
        assertNotEquals(userDAO.getUser(0).username(), null);
    }

    @Test
    public void goodLoginRespond() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LoginRequest req = new LoginRequest("TestUser", "Pass");
        LoginResponse testResp = userService.loginRespond(req, userDAO, authDAO);

        assertEquals(userDAO.getUser(0).password(), req.password);
    }

    @Test
    public void badLoginRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LoginRequest badReq = new LoginRequest("BadUser", "Pass");
        LoginResponse testResp = userService.loginRespond(badReq, userDAO, authDAO);

        assertNotEquals(null, userDAO.getUser(0).password());
    }

    @Test
    public void goodLogoutRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LogoutResponse testResp = userService.logoutRespond(authDAO.getAuthByID(0).authToken(), authDAO);

        assertEquals(200, testResp.status);
    }

    @Test
    public void badLogoutRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LogoutResponse testResp = userService.logoutRespond("auth", authDAO);

        assertNotEquals(authDAO.getSize(), 0);
    }

    @Test
    public void goodClearRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        ClearResponse testResp = dbService.clearRespond(userDAO, authDAO, gameDAO);

        assertEquals(userDAO.getSize() - 1, 0);
        assertEquals(authDAO.getSize() - 1, 0);
        assertEquals(gameDAO.getSize() - 1, 0);
    }

    @Test
    public void badClearRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        ClearResponse testResp = dbService.clearRespond(userDAO, authDAO, gameDAO);

        assertNotEquals(userDAO.getSize() - 1, 1);
        assertNotEquals(authDAO.getSize() - 1, 1);
        assertNotEquals(gameDAO.getSize() - 1, 1);
    }

    @Test
    public void goodCreateGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResponse resp = gameService.createGameRespond(req, authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);

        assertEquals(gameDAO.getGame(0).gameName(), req.gameName);
    }

    @Test
    public void badCreateGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResponse resp = gameService.createGameRespond(req, "BadAuthToken", authDAO, gameDAO);

        assertNotEquals(req.gameName, gameDAO.getSize());
    }

    @Test
    public void goodListGamesRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);
        ListGamesResponse resp = gameService.listGamesRespond(authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);

        assertEquals(200, resp.status);
    }

    @Test
    public void badListGamesRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);
        ListGamesResponse resp = gameService.listGamesRespond("BadAuthToken", authDAO, gameDAO);

        assertNotEquals(resp.games, gameDAO.returnGameList());
    }

    @Test
    public void goodJoinGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.getGame(0).gameID());
        JoinGameResponse resp = gameService.joinGameRespond(req, authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);

        assertEquals(200, resp.status);
    }

    @Test
    public void badJoinGameRequest() throws DataAccessException {
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuthByID(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.getGame(0).gameID());
        JoinGameResponse resp = gameService.joinGameRespond(req, "BadAuthToken", authDAO, gameDAO);

        assertNotEquals(200, resp.status);
    }
}
