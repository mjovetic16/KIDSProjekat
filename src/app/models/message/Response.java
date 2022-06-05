package app.models.message;

import app.ServentInfo;
import app.models.Node;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {


    @Serial
    private static final long serialVersionUID = -2642566603005301854L;



    private ResponseType responseType;

    private boolean accepted;

    private Object data;

    private Node sender;


    public Node getSender() {
        return sender;
    }

    public void setSender(Node sender) {
        this.sender = sender;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
