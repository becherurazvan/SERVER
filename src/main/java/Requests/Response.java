package Requests;

/**
 * Created by rbech on 2/6/2016.
 */
public class Response {
    boolean succesful;
    String message;

    public Response(boolean succesful, String message) {
        this.succesful = succesful;
        this.message = message;
    }


    public Response() {
    }

    public boolean isSuccesful() {
        return succesful;
    }

    public String getMessage() {
        return message;
    }
}
