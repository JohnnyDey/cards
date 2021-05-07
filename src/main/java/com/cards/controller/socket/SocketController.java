package com.cards.controller.socket;

import com.cards.controller.messageresolver.ResolverFactory;
import com.cards.controller.socket.message.InputMessage;
import com.cards.database.CacheStorage;
import com.cards.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class SocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CacheStorage storage;
    @Autowired
    private ResolverFactory resolverFactory;

    @MessageMapping("/game/{gameId}")
    public void handleGameMessage(Principal principal, InputMessage msg, @DestinationVariable String gameId) {
        try {
            log.info("Message {} received", msg.getType());
            msg.setSenderUid(principal.getName());
            msg.setGameUid(gameId);
            resolverFactory.getResolver(msg).prepare().apply();
        } catch (Exception e) {
            log.error("Exception handled", e);
            resolverFactory.getExceptionResolver(e.getMessage(), msg).prepare().apply();
        }
    }

    @MessageMapping("/common/createGame")
    public void createGame(Principal principal) {
        Game game = storage.createGame();
        messagingTemplate.convertAndSendToUser(principal.getName(),
                "/common",
                game.getUid());
    }

}