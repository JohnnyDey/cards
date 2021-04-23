package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.model.Player;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

@Slf4j
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
    public abstract OutputMessage buildMessage(String uid);
    public OutputMessage buildReplyMessage(String uid) {
        return buildMessage(uid);
    }

    public static String getDestination(String gameId){
        return "/game/" + gameId + "/subscriber";
    }

    protected void sendMessageToPlayers() {
        Map<String, Player> players = gameController.getGame().getPlayers();
        String destination = getDestination(inputMessage.getGameUid());
        players.forEach((uid, player) -> sendMessage(uid, destination));
    }

    private void sendMessage(String receiveUid, String destination){
        OutputMessage msg = receiveUid.equals(inputMessage.getSenderUid()) ? buildReplyMessage(receiveUid) : buildMessage(receiveUid);
        if (msg != null) {
            log.info("Message {} send to {}", msg.getType(), receiveUid);
            messagingTemplate.convertAndSendToUser(receiveUid, destination, msg);
        }
    }

}
