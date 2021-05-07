package com.cards.controller.messageresolver;

import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Game;
import com.cards.model.Player;
import com.cards.model.card.WhiteCard;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("CHOSE_BEST")
public class ChoseCardResolver extends AbstractResolver {
    private Player winner;

    @Override
    public void apply() {
        winner = gameController.chooseWinner(inputMessage.getCardUid(), inputMessage.getSenderUid());
        gameController.endRound();
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildMessage(String receiveUid){
        Game game = gameController.getGame();
        Player player = game.getPlayers().get(receiveUid);

        OutputMessage message = new OutputMessage(OutputMessage.MessageType.NEXT_ROUND);
        message.setCard(game.getQuestion());
        message.setCards(player.getCards().values().toArray(new WhiteCard[0]));
        message.setPlayers(game.getPlayers().values().toArray(new Player[0]));
        message.setDetail(gameController.getLeader().getUsername());
        return message;
    }
}
