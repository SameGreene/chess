package response;

public class JoinGameResponse {
    public String message;
    public int status;

    public JoinGameResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
