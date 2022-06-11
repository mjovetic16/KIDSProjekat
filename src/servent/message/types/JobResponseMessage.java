package servent.message.types;

import app.ServentInfo;
import app.models.message.Response;
import servent.message.MessageType;

public class JobResponseMessage extends BasicMessage{

    private Response response;


    public JobResponseMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, Response response) {
        super(MessageType.JOB_RESPONSE, originalSenderInfo, receiverInfo, response.isAccepted()+"");
        this.response = response;
    }


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
