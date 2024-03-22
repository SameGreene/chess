package response;

public class LogoutResponse {
    public String message;
    public int status;

    public LogoutResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
