package com.cards.controller.messageresolver;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("REMOVE_USER")
public class RemoveUserResolver extends UpdateUsersResolver{
    @Override
    void updateUsers() {
        gameController.removePlayer(inputMessage.getSenderUid());
    }
}
