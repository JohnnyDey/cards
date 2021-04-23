package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.card.Card;

public class ChoseOwnCardResolver extends Resolver {
    private Card answer;
    public ChoseOwnCardResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }
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
