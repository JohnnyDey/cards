package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Player;
import com.cards.model.card.Card;

import java.lang.reflect.Array;
import java.util.Collections;

public class AddUserResolver extends UpdateUsersResolver{
    public AddUserResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }

    @Override
    void updateUsers() {
        gameController.addPlayer(inputMessage.getSenderUid());
    }

    @Override
    public OutputMessage buildMessage() {
        OutputMessage msg = super.buildMessage();
        Player player = gameController.getGame().getPlayers().get(inputMessage.getSenderUid());

        Card[] card = new Card[player.getCards().size()];
        msg.setCards(player.getCards().values().toArray(card));
        return msg;
    }
}
