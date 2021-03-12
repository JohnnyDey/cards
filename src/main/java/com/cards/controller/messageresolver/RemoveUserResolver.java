package com.cards.controller.messageresolver;

import com.cards.controller.GameController;
import com.cards.controller.socket.message.InputMessage;

public class RemoveUserResolver extends UpdateUsersResolver{
    public RemoveUserResolver(GameController gameController, InputMessage inputMessage) {
        super(gameController, inputMessage);
    }
    @Override
    void updateUsers() {
        gameController.removePlayer(inputMessage.getSenderUid());
    }
}
