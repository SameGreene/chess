package clientTests;

import org.junit.jupiter.api.*;
import response.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(3000);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void goodClearTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();

        assertEquals(1, server.authObj.getSize());
    }

    @Test
    public void badClearTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();

        assertNotEquals(0, server.authObj.getSize());
    }

    @Test
    public void goodRegisterTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("sam", "sam", "test@email.com");

        assertEquals(server.userObj.getUser(0).username(), "sam");
    }

    @Test
    public void badRegisterTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("sam", "sam", "testing@email.com");

        assertNotEquals(null, server.userObj.getUser(0));
    }

    @Test
    public void goodLoginTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        String password = "kj";
        LoginResponse loginResponse = serverFacade.login("jk", password);

        assertEquals(200, loginResponse.status);
    }

    @Test
    public void badLoginTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        String password = "kj";
        LoginResponse loginResponse = serverFacade.login("jk", password);

        assertNotEquals(400, loginResponse.status);
    }

    @Test
    public void goodLogoutTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        String password = "kj";
        LoginResponse loginResponse = serverFacade.login("jk", password);
        LogoutResponse logoutResponse = serverFacade.logout();

        assertEquals(200, logoutResponse.status);
    }

    @Test
    public void badLogOutTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        String password = "kj";
        LoginResponse loginResponse = serverFacade.login("jk", password);
        LogoutResponse logoutResponse = serverFacade.logout();

        assertNotEquals(400, logoutResponse.status);
    }

    @Test
    public void goodListTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        serverFacade.create("yee");
        ListGamesResponse listGamesResponse = serverFacade.list();

        assertEquals(200, listGamesResponse.status);
    }

    @Test
    public void badListTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        serverFacade.create("yee");
        ListGamesResponse listGamesResponse = serverFacade.list();

        assertNotEquals(400, listGamesResponse.status);
    }

    @Test
    public void goodCreateTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        CreateGameResponse createGameResponse = serverFacade.create("yee");

        assertEquals(200, createGameResponse.status);
    }

    @Test
    public void badCreateTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        CreateGameResponse createGameResponse = serverFacade.create("yee");

        assertNotEquals(400, createGameResponse.status);
    }

    @Test
    public void goodJoinTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        CreateGameResponse createGameResponse = serverFacade.create("yee");
        JoinGameResponse joinGameResponse = serverFacade.join("WHITE", 1);

        assertEquals(200, joinGameResponse.status);
    }

    @Test
    public void badJoinTest() throws Exception {
        ServerFacade serverFacade = new ServerFacade("http://localhost", 3000);
        serverFacade.clearDatabase();
        serverFacade.register("jk", "kj", "test@gmail.com");
        CreateGameResponse createGameResponse = serverFacade.create("yee");
        JoinGameResponse joinGameResponse = serverFacade.join("WHITE", 1);

        assertNotEquals(400, joinGameResponse.status);
    }
}
