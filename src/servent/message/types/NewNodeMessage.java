package servent.message.types;

import app.ServentInfo;
import servent.message.MessageType;

import java.io.Serial;

public class NewNodeMessage extends BasicMessage {

	@Serial
	private static final long serialVersionUID = -712115187051859365L;


	public NewNodeMessage(ServentInfo senderPort, ServentInfo receiverPort) {
		super(MessageType.NEW_NODE, senderPort, receiverPort);
	}


}
