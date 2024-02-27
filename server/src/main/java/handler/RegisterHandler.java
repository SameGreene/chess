package handler;

import com.google.gson.Gson;
import request.RegisterRequest;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handle (Request request, Response response){
        Gson myGson = new Gson();
        RegisterRequest myRequest = myGson.fromJson(request.body(), RegisterRequest.class);
        RegisterService myService = new RegisterService();
        myService.respond(myRequest);
    }
}
