package servent.message.types;

import app.ServentInfo;
import app.models.message.Response;
import servent.message.MessageType;

import java.io.Serial;

public class StopMessage extends BasicMessage{

    @Serial
    private static final long serialVersionUID = 521589686445393508L;

    public StopMessage(ServentInfo sender, ServentInfo receiver, String jobName) {
        super(MessageType.STOP, sender, receiver, jobName);

    }
}
