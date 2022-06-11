package servent.message.types;

import app.ServentInfo;
import app.models.message.NewNodeData;
import servent.message.MessageType;

public class WelcomeMessage extends BasicMessage{

    private NewNodeData newNodeData;


    public WelcomeMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, NewNodeData newNodeData) {
        super(MessageType.WELCOME, originalSenderInfo, receiverInfo);
        this.newNodeData = newNodeData;
    }


    public NewNodeData getNewNodeData() {
        return newNodeData;
    }

    public void setNewNodeData(NewNodeData newNodeData) {
        this.newNodeData = newNodeData;
    }
}
