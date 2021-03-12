package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.card.Card;

public class ChoseOwnCardResolver extends Resolver {
    public ChoseOwnCardResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }
    @Override
    public void apply() {
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildMessage(){
        Card card = gameController.answer(inputMessage.getSenderUid(), inputMessage.getCardUid());
        OutputMessage message = new OutputMessage(OutputMessage.MessageType.NEW_ANSWER);
        message.setCard(card);
        return message;
    }
}
