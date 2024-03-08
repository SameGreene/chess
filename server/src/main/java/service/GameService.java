package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.JoinGameResponse;

public class GameService {
    public ListGamesResponse listGamesRespond(String authToken, AuthDAO authObj, GameDAO gameObj) throws DataAccessException {
        boolean authenticated = false;
        // Check for authentication
        for (int i = 0; i < authObj.getSize(); i = i + 1) {
            if (authObj.getAuthByID(i) == null) continue;
            if (authToken.equals(authObj.getAuthByID(i).authToken())) {
                authenticated = true;
                break;
            }
        }
        if (authenticated) {
            return new ListGamesResponse(gameObj.returnGameList(), "", 200);
        } else {
            return new ListGamesResponse(null, "ERROR - Unauthorized", 401);
        }
    }

    public CreateGameResponse createGameRespond(CreateGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj) throws DataAccessException {
        boolean authenticated = false;

        if (req.getGameName() == null) {
            return new CreateGameResponse(null, "ERROR - Bad Request", 400);
        }
        // Check for authentication
        for (int i = 0; i < authObj.getSize(); i = i + 1) {
            if (authObj.getAuthByID(i) == null) continue;
            if (authObj.getAuthByID(i).authToken().equals(authToken)) {
                authenticated = true;
                break;
            }
        }

        if (authenticated) {
            int newGameID = gameObj.getCurrentID();
            GameData gameDataToAdd = new GameData(newGameID, null, null, req.getGameName(), new ChessGame());
            gameObj.createGame(gameDataToAdd);
            gameObj.setCurrentID(gameObj.getCurrentID() + 1);
            return new CreateGameResponse(newGameID, null, 200);
        } else {
            return new CreateGameResponse(null, "ERROR - Unauthorized", 401);
        }
    }

    public JoinGameResponse joinGameRespond(JoinGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj) throws DataAccessException {
        boolean authenticated = false;
        int userNumber = 0;

        if (req.gameID == 0) {
            return new JoinGameResponse("ERROR - Bad Request", 400);
        } else {
            // Check for authentication
            for (int i = 0; i < authObj.getSize(); i = i + 1) {
                if (authObj.getAuthByID(i) == null) continue;
                if (authObj.getAuthByID(i).authToken().equals(authToken)) {
                    authenticated = true;
                    userNumber = i;
                    break;
                }
            }
        }

        if (authenticated) {
            for (int i = 0; i < gameObj.getSize(); i = i + 1) {
                // Does the game exist?
                if (gameObj.getGame(i) == null) continue;
                if (gameObj.getGame(i).gameID() == req.getGameID()) {
                    if (req.getPlayerColor() == null) {
                        // Joins as observer
                        return new JoinGameResponse(null, 200);
                    } else {
                        if (req.getPlayerColor() != null && req.getPlayerColor().equals("WHITE") && gameObj.getGame(i).whiteUsername() == null) {
                            String newWhite = authObj.getAuthByID(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), newWhite, gameObj.getGame(i).blackUsername(), gameObj.getGame(i).gameName(), gameObj.getGame(i).game());
                            gameObj.setGame(i, gameToUpdate);
                            return new JoinGameResponse("", 200);
                        } else if (req.getPlayerColor() != null && req.getPlayerColor().equals("BLACK") && gameObj.getGame(i).blackUsername() == null) {
                            String newBlack = authObj.getAuthByID(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), gameObj.getGame(i).whiteUsername(), newBlack, gameObj.getGame(i).gameName(), gameObj.getGame(i).game());
                            gameObj.setGame(i, gameToUpdate);
                            return new JoinGameResponse("", 200);
                        } else {
                            return new JoinGameResponse("ERROR - Already taken", 403);
                        }
                    }
                }
            }
        }
        return new JoinGameResponse("ERROR - Unauthorized", 401);
    }
}
