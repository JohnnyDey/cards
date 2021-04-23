package com.cards;

import com.cards.controller.GameController;
import com.cards.database.CacheStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class SocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private GameController gameController;
    @Autowired
    private CacheStorage storage;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/game")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent disconnectEvent){
        Principal user = disconnectEvent.getUser();
        if(user != null){
            gameController.setGame(storage.getGameByUserUid(user.getName()));
            gameController.removePlayer(user.getName());
        }
    }
}

