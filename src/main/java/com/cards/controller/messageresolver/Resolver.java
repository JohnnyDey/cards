package com.cards.controller.messageresolver;

import com.cards.controller.socket.message.InputMessage;

public interface Resolver {
    void apply();
    Resolver prepare();
    void setInputMessage(InputMessage message);
}
