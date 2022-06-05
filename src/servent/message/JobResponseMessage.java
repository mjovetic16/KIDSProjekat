package servent.message;

import app.ServentInfo;
import app.models.message.Response;

public class JobResponseMessage extends BasicMessage{

    private Response response;


    public JobResponseMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, Response response) {
        super(MessageType.JOB_RESPONSE, originalSenderInfo, receiverInfo);
        this.response = response;
    }


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
