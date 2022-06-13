package servent.message.types;

import app.ServentInfo;
import app.models.message.Response;
import servent.message.MessageType;

import java.io.Serial;

public class StatusMessage extends BasicMessage{

    @Serial
    private static final long serialVersionUID = 4452467686344097362L;

    private Response response;

    public StatusMessage(ServentInfo sender, ServentInfo receiver, Response response) {
        super(MessageType.STATUS, sender, receiver, response.getResponseType().toString());
        this.response = response;

    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
