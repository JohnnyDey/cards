package com.cards.controller.messageresolver;

import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Player;

import java.util.Map;

public abstract class UpdateUsersResolver extends AbstractResolver {

    abstract void updateUsers();

    @Override
    public void apply() {
        updateUsers();
        sendMessageToPlayers();
    }

    @Override
    public OutputMessage buildMessage(String receiveUid){
        Map<String, Player> players = gameController.getGame().getPlayers();
        Player[] playersArray = new Player[players.size()];
        players.values().toArray(playersArray);

        OutputMessage message = new OutputMessage(OutputMessage.MessageType.PLAYER_LIST_UPDATED);
        message.setPlayers(playersArray);
        message.setGameStatus(gameController.getGame().getStatus().toString());
        return message;
    }
}
