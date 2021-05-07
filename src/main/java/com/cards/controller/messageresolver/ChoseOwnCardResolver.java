package com.cards.controller.messageresolver;

import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.card.Card;
import org.springframework.stereotype.Component;

@Component("CHOOSE_OWN")
public class ChoseOwnCardResolver extends AbstractResolver {
    private Card answer;

    @Override
    public void apply() {
        answer = gameController.answer(inputMessage.getSenderUid(), inputMessage.getCardUid());
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildMessage(String receiveUid){
        OutputMessage message = new OutputMessage(OutputMessage.MessageType.NEW_ANSWER);
        message.setCard(answer);
        message.setDetail(inputMessage.getSenderUid());
        return message;
    }
}
