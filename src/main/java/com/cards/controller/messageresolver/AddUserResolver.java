package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Game;
import com.cards.model.Player;
import com.cards.model.card.Card;

public class AddUserResolver extends UpdateUsersResolver{
    public AddUserResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }

    @Override
    void updateUsers() {
        if (gameController.getGame().getPlayers().containsKey(inputMessage.getSenderUid())) {
            throw new IllegalArgumentException("Игрок уже в игре");
        }
        gameController.addPlayer(inputMessage.getSenderUid(), inputMessage.getDetail());
    }

    @Override
    public OutputMessage buildReplyMessage(String uid) {
        OutputMessage msg = super.buildReplyMessage(uid);
        Game game = gameController.getGame();
        Player player = game.getPlayers().get(inputMessage.getSenderUid());

        Card[] card = new Card[player.getCards().size()];
        msg.setCards(player.getCards().values().toArray(card));
        msg.setCard(game.getQuestion());
        Player leader = game.getOrder().peek();
        if (leader != null) {
            msg.setDetail(leader.getUsername());
        }
        return msg;
    }
}
