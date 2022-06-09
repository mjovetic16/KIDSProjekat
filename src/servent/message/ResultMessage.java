package servent.message;

import app.ServentInfo;
import app.models.job.ActiveJob;
import app.models.message.Response;
import app.models.message.ResponseType;

import java.io.Serial;

public class ResultMessage extends BasicMessage{

    @Serial
    private static final long serialVersionUID = 4452467686344097362L;

    private Response response;

    public ResultMessage(ServentInfo sender, ServentInfo receiver, Response response) {
        super(MessageType.RESULT, sender, receiver, response.getResponseType().toString());
        this.response = response;

    }



    @Override
    public void sendEffect() {


    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
