package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.BeforeAll;
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

public class APIServiceTests {


    private UserService userService = new UserService();
    private DBService dbService = new DBService();
    private GameService gameService = new GameService();

    private UserDAO userDAO = new UserDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();
    private GameDAO gameDAO = new GameDAO();

    @BeforeEach
    public void setup() {
        userDAO.clearUserList();
        authDAO.clearAuthList();
        gameDAO.clearGameList();
    }

    @Test
    public void goodRegRespond(){
        RegisterRequest req = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse testResp = userService.regRespond(req, userDAO, authDAO);

        assertEquals(authDAO.getAuth(0).username(), req.username);
        assertEquals(userDAO.userList.get(0).username(), req.username);
    }

    @Test
    public void badRegRespond(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterRequest badReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse testResp = userService.regRespond(badReq, userDAO, authDAO);

        assertNotEquals(authDAO.getAuth(0).username(), null);
        assertNotEquals(userDAO.userList.get(0).username(), null);
    }

    @Test
    public void goodLoginRespond(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LoginRequest req = new LoginRequest("TestUser", "Pass");
        LoginResponse testResp = userService.loginRespond(req, userDAO, authDAO);

        assertEquals(userDAO.userList.get(0).password(), req.password);
    }

    @Test
    public void badLoginRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LoginRequest badReq = new LoginRequest("BadUser", "Pass");
        LoginResponse testResp = userService.loginRespond(badReq, userDAO, authDAO);

        assertNotEquals(null, userDAO.userList.get(0).password());
    }

    @Test
    public void goodLogoutRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LogoutResponse testResp = userService.logoutRespond(authDAO.getAuth(0).authToken(), authDAO);

        assertEquals(authDAO.getSize(), 0);
    }

    @Test
    public void badLogoutRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        LogoutResponse testResp = userService.logoutRespond("auth", authDAO);

        assertNotEquals(authDAO.getSize(), 0);
    }

    @Test
    public void goodClearRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        ClearResponse testResp = dbService.clearRespond(userDAO, authDAO, gameDAO);

        assertEquals(userDAO.userList.size(), 0);
        assertEquals(authDAO.getSize(), 0);
        assertEquals(gameDAO.gameList.size(), 0);
    }

    @Test
    public void badClearRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        ClearResponse testResp = dbService.clearRespond(userDAO, authDAO, gameDAO);

        assertNotEquals(userDAO.userList.size(), 1);
        assertNotEquals(authDAO.getSize(), 1);
        assertNotEquals(gameDAO.gameList.size(), 1);
    }

    @Test
    public void goodCreateGameRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResponse resp = gameService.createGameRespond(req, authDAO.getAuth(0).authToken(), authDAO, gameDAO);

        assertEquals(gameDAO.gameList.get(0).gameName(), req.gameName);
    }

    @Test
    public void badCreateGameRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest req = new CreateGameRequest("Game");
        CreateGameResponse resp = gameService.createGameRespond(req, "BadAuthToken", authDAO, gameDAO);

        assertNotEquals(req.gameName, gameDAO.gameList.size());
    }

    @Test
    public void goodListGamesRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuth(0).authToken(), authDAO, gameDAO);
        ListGamesResponse resp = gameService.listGamesRespond(authDAO.getAuth(0).authToken(), authDAO, gameDAO);

        assertEquals(gameDAO.gameList.size(), 1);
    }

    @Test
    public void badListGamesRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuth(0).authToken(), authDAO, gameDAO);
        ListGamesResponse resp = gameService.listGamesRespond("BadAuthToken", authDAO, gameDAO);

        assertNotEquals(resp.games, gameDAO.gameList);
    }

    @Test
    public void goodJoinGameRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuth(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.gameList.get(0).gameID());
        JoinGameResponse resp = gameService.joinGameRespond(req, authDAO.getAuth(0).authToken(), authDAO, gameDAO);

        assertEquals(200, resp.status);
    }

    @Test
    public void badJoinGameRequest(){
        RegisterRequest goodReq = new RegisterRequest("TestUser", "Pass", "my@mail.com");
        RegisterResponse goodResp = userService.regRespond(goodReq, userDAO, authDAO);
        CreateGameRequest createReq = new CreateGameRequest("Game");
        CreateGameResponse createResp = gameService.createGameRespond(createReq, authDAO.getAuth(0).authToken(), authDAO, gameDAO);
        JoinGameRequest req = new JoinGameRequest("WHITE", gameDAO.gameList.get(0).gameID());
        JoinGameResponse resp = gameService.joinGameRespond(req, "BadAuthToken", authDAO, gameDAO);

        assertNotEquals(200, resp.status);
    }
}
