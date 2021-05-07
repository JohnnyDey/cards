package com.cards.controller.messageresolver;

import com.cards.controller.socket.message.OutputMessage;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component("EXCEPTION")
public class ExceptionResolver extends AbstractResolver{
    @Setter
    private String exceptionDetail;
    @Override
    public void apply() {
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildReplyMessage(String uid) {
        OutputMessage payload = new OutputMessage(OutputMessage.MessageType.EXCEPTION);
        payload.setDetail(exceptionDetail);
        return payload;
    }

    @Override
    public OutputMessage buildMessage(String uid) {
        return null;
    }
}
