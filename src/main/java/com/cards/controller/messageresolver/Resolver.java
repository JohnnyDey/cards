package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Player;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public abstract class Resolver {
    @Setter
    protected SimpMessagingTemplate messagingTemplate;
    protected final GameController gameController;
    protected final InputMessage inputMessage;

    public Resolver(GameController gameController, InputMessage inputMessage){
        this.gameController = gameController;
        this.inputMessage = inputMessage;
    }
    public abstract void apply();
    public abstract OutputMessage buildMessage();

    public static String getDestination(String gameId){
        return "/game/" + gameId + "/subscriber";
    }

    protected void sendMessageToPlayers() {
        Map<String, Player> players = gameController.getGame().getPlayers();
        String destination = getDestination(inputMessage.getGameUid());
        players.forEach((uid, player) ->
                messagingTemplate.convertAndSendToUser(uid, destination, buildMessage()));
    }

}
