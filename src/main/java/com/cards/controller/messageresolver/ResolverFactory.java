package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;
import com.cards.database.CacheStorage;
import com.cards.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ResolverFactory {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private GameController gameController;
    @Autowired
    private CacheStorage storage;

    public Resolver getResolver(InputMessage inputMessage){
        Game game = storage.getGameByUid(inputMessage.getGameUid());
        gameController.setGame(game);
        Resolver resolver;
        switch (inputMessage.getType()){
            case ADD_USER:
                resolver = new AddUserResolver(gameController, inputMessage);
                break;
            case REMOVE_USER:
                resolver = new RemoveUserResolver(gameController, inputMessage);
                break;
            case CHOOSE_OWN:
                resolver = new ChoseOwnCardResolver(gameController, inputMessage);
                break;
            case CHOSE_BEST:
                resolver = new ChoseCardResolver(gameController, inputMessage);
                break;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }
        resolver.setMessagingTemplate(messagingTemplate);
        return resolver;
    }
}
