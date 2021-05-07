package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;

public class StartGameResolver extends ChoseCardResolver{
    public StartGameResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }

    @Override
    public void apply() {
        gameController.endRound();
        sendMessageToPlayers();
    }
}
