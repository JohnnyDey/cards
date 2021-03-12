package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Game;
import com.cards.model.Player;
import com.cards.model.card.Card;

public class ChoseCardResolver extends Resolver {
    public ChoseCardResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }
    @Override
    public void apply() {
        gameController.chooseWinner(inputMessage.getCardUid());
        gameController.endRound();
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildMessage(){
        Game game = gameController.getGame();
        Player player = game.getPlayers().get(inputMessage.getSenderUid());
        Card card = player.getCards().get(inputMessage.getCardUid());

        OutputMessage message = new OutputMessage(OutputMessage.MessageType.NEXT_ROUND);
        message.setPlayers(new Player[]{player});
        message.setCard(card);
        return message;
    }
}
