package com.cards.controller.socket;

import com.cards.controller.messageresolver.Resolver;
import com.cards.controller.messageresolver.ResolverFactory;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.database.CacheStorage;
import com.cards.model.Game;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class SocketController {
    @Autowired
    private ResolverFactory resolverFactory;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CacheStorage storage;

    @MessageMapping("/game/{gameId}")
    public void handleGameMessage(Principal principal, InputMessage msg, @DestinationVariable String gameId) {
        try {
            log.info("Message {} received", msg.getType());
            msg.setSenderUid(principal.getName());
            msg.setGameUid(gameId);
            resolverFactory.getResolver(msg).apply();
        } catch (Exception e) {
            log.error("Exception handled", e);
            OutputMessage payload = new OutputMessage(OutputMessage.MessageType.EXCEPTION);
            payload.setDetail(e.getMessage());
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    Resolver.getDestination(gameId),
                    payload);
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