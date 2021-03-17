package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Game;
import com.cards.model.Player;
import com.cards.model.card.Card;

public class ChoseCardResolver extends Resolver {
    private Player winner;
    public ChoseCardResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }
    @Override
    public void apply() {
        winner = gameController.chooseWinner(inputMessage.getCardUid());
        gameController.endRound();
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildMessage(String receiveUid){
        Game game = gameController.getGame();
        Player player = game.getPlayers().get(inputMessage.getSenderUid());
        Card card = player.getCards().get(inputMessage.getCardUid());

        OutputMessage message = new OutputMessage(OutputMessage.MessageType.NEXT_ROUND);
        message.setCards((Card[]) player.getCards().values().toArray());
        message.setCard(card);
        message.setPlayers(new Player[]{winner});
        return message;
    }
}
