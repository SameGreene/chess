package response;

public class RegisterResponse {
    public String username;
    public String authToken;
    public String message;

    public RegisterResponse(String username, String authToken, String message) {
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }
}
