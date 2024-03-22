package response;

public class CreateGameResponse {
    public Integer gameID;
    public String message;
    public int status;

    public CreateGameResponse(Integer gameID, String message, int status) {
        this.gameID = gameID;
        this.message = message;
        this.status = status;
    }
}
