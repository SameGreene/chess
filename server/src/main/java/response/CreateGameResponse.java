package response;

public class CreateGameResponse {
    public int gameID;
    public String message;
    public int status;

    public CreateGameResponse(int gameID, String message, int status) {
        this.gameID = gameID;
        this.message = message;
        this.status = status;
    }
}
