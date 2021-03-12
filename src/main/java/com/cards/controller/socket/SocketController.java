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
            resolverFactory.getResolver(msg).apply();
        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    Resolver.getDestination(gameId),
                    new OutputMessage(OutputMessage.MessageType.EXCEPTION));
        }
    }

}