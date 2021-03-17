package com.cards.controller.socket;

import com.cards.controller.messageresolver.Resolver;
import com.cards.controller.messageresolver.ResolverFactory;
import com.cards.controller.socket.message.InputMessage;
import com.cards.controller.socket.message.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class SocketController {
    @Autowired
    private ResolverFactory resolverFactory;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/game/{gameId}")
    public void send(Principal principal, InputMessage msg, @DestinationVariable String gameId) {
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

}