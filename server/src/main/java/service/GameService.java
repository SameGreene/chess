package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;

import java.util.Random;

public class GameService {
    public ListGamesResponse listGamesRespond(String authToken, AuthDAO authObj, GameDAO gameObj){
        boolean authenticated = false;
        // Check for authentication
        for (int i = 0; i < authObj.authList.size(); i = i + 1){
            if(authToken.equals(authObj.authList.get(i).authToken())){
                authenticated = true;
                break;
            }
        }
        if(authenticated){
            return new ListGamesResponse(gameObj.gameList, "", 200);
        }
        else{
            return new ListGamesResponse(null, "ERROR - Unauthorized", 401);
        }
    }

    public CreateGameResponse createGameRespond(CreateGameRequest req, String authToken, AuthDAO authObj, GameDAO gameObj){
        boolean authenticated = false;

        if (req.getGameName() == null){
            return new CreateGameResponse(0, "ERROR - Bad Request", 400);
        }
        else{
            // Check for authentication
            for (int i = 0; i < authObj.authList.size(); i = i + 1){
                if(authObj.authList.get(i).authToken().equals(authToken)){
                    authenticated = true;
                    break;
                }
            }
        }

        if(authenticated){
            int newGameID = gameObj.currentID;
            gameObj.currentID = gameObj.currentID + 1;
            GameData gameDataToAdd = new GameData(newGameID, null, null, req.getGameName(), new ChessGame());
            return new CreateGameResponse(newGameID, null, 200);
        }
        else {
            return new CreateGameResponse(0, "ERROR - Unauthorized", 401);
        }
    }
}
