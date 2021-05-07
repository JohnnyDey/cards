package com.cards.controller.messageresolver;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("START")
public class StartGameResolver extends ChoseCardResolver{

    @Override
    public void apply() {
        gameController.endRound();
        sendMessageToPlayers();
    }
}
