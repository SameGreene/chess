package ui;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;

import request.*;
import response.*;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url, int port) {
        serverUrl = url + ":" + port;
    }

    public ClearResponse clearDatabase() throws Exception {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, ClearResponse.class, null);
    }

    public RegisterResponse register(String username, String password, String email) throws Exception {
        var path = "/user";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResponse retResponse = this.makeRequest("POST", path, registerRequest, RegisterResponse.class, null);
        authToken = retResponse.authToken;
        return retResponse;
    }

    public LoginResponse login(String username, String password) throws Exception {
        var path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResponse retResponse =  this.makeRequest("POST", path, loginRequest, LoginResponse.class, authToken);
        authToken = retResponse.authToken;
        return retResponse;
    }

    public LogoutResponse logout() throws Exception {
        var path = "/session";
        return this.makeRequest("DELETE", path, null, LogoutResponse.class, authToken);
    }

    public ListGamesResponse list() throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResponse.class, authToken);
    }

    public CreateGameResponse create(String gameName) throws Exception {
        var path = "/game";
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        return this.makeRequest("POST", path, createGameRequest, CreateGameResponse.class, authToken);
    }

    public JoinGameResponse join(String playerColor, int gameID) throws Exception {
        var path = "/game";
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
        return this.makeRequest("PUT", path, joinGameRequest, JoinGameResponse.class, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws Exception {
        URL url = (new URI(serverUrl + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);

        if (authToken != null && !authToken.isEmpty()) {
            http.setRequestProperty("authorization", authToken);
        }

        writeBody(request, http);
        http.connect();
        throwIfNotSuccessful(http);
        return readBody(http, responseClass);
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("500 - Internal server error");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
