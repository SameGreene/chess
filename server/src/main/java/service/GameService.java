package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.JoinGameResponse;

public class GameService {
    public ListGamesResponse listGamesRespond(String authToken, AuthDAO authObj, GameDAO gameObj) {
        boolean authenticated = false;
        // Check for authentication
        for (int i = 0; i < authObj.authList.size(); i = i + 1) {
            if (authToken.equals(authObj.authList.get(i).authToken())) {
                authenticated = true;
                break;
            }
        }
        if (authenticated) {
            return new ListGamesResponse(gameObj.gameList, "", 200);
        } else {
            return new ListGamesResponse(null, "ERROR - Unauthorized", 401);
        }
    }

    public CreateGameResponse createGameRespond(CreateGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj) {
        boolean authenticated = false;

        if (req.getGameName() == null) {
            return new CreateGameResponse(null, "ERROR - Bad Request", 400);
        }
        // Check for authentication
        for (int i = 0; i < authObj.authList.size(); i = i + 1) {
            if (authObj.authList.get(i).authToken().equals(authToken)) {
                authenticated = true;
                break;
            }
        }

        if (authenticated) {
            int newGameID = gameObj.currentID;
            gameObj.currentID = gameObj.currentID + 1;
            GameData gameDataToAdd = new GameData(newGameID, null, null, req.getGameName(), new ChessGame());
            gameObj.gameList.add(gameDataToAdd);
            return new CreateGameResponse(newGameID, null, 200);
        } else {
            return new CreateGameResponse(null, "ERROR - Unauthorized", 401);
        }
    }

    public JoinGameResponse joinGameRespond(JoinGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj) {
        boolean authenticated = false;
        int userNumber = 0;

        if (req.gameID == 0) {
            return new JoinGameResponse("ERROR - Bad Request", 400);
        } else {
            // Check for authentication
            for (int i = 0; i < authObj.authList.size(); i = i + 1) {
                if (authObj.authList.get(i).authToken().equals(authToken)) {
                    authenticated = true;
                    userNumber = i;
                    break;
                }
            }
        }

        if (authenticated) {
            for (int i = 0; i < gameObj.gameList.size(); i = i + 1) {
                // Does the game exist?
                if (gameObj.gameList.get(i).gameID() == req.getGameID()) {
                    if (req.getPlayerColor() == null) {
                        // Joins as observer
                        return new JoinGameResponse(null, 200);
                    } else {
                        if (req.getPlayerColor() != null && req.getPlayerColor().equals("WHITE") && gameObj.gameList.get(i).whiteUsername() == null) {
                            String newWhite = authObj.authList.get(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), newWhite, gameObj.gameList.get(i).blackUsername(), gameObj.gameList.get(i).gameName(), gameObj.gameList.get(i).game());
                            gameObj.gameList.set(i, gameToUpdate);
                            return new JoinGameResponse("", 200);
                        } else if (req.getPlayerColor() != null && req.getPlayerColor().equals("BLACK") && gameObj.gameList.get(i).blackUsername() == null) {
                            String newBlack = authObj.authList.get(userNumber).username();
                            GameData gameToUpdate = new GameData(req.getGameID(), gameObj.gameList.get(i).whiteUsername(), newBlack, gameObj.gameList.get(i).gameName(), gameObj.gameList.get(i).game());
                            gameObj.gameList.set(i, gameToUpdate);
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
