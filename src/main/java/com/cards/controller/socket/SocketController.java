package com.cards.controller.socket;

import com.cards.controller.messageresolver.Resolver;
import com.cards.controller.messageresolver.ResolverFactory;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import com.cards.database.CacheStorage;
import com.cards.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
            msg.setSenderUid(principal.getName());
            msg.setGameUid(gameId);
            resolverFactory.getResolver(msg).apply();
        } catch (Exception e) {
            OutputMessage payload = new OutputMessage(OutputMessage.MessageType.EXCEPTION);
            payload.setDetail(e.getMessage());
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    Resolver.getDestination(gameId),
                    payload);
        }
    }

    @MessageMapping("/common/getGames")
    @SendTo("/common")
    public List<String> getGames() {
        return storage.getPublicGames().stream().map(Game::getUid).collect(Collectors.toList());
    }

    @MessageMapping("/common/createGame")
    @SendTo("/common")
    public String createGame() {
        return storage.createGame().getUid();
    }

}