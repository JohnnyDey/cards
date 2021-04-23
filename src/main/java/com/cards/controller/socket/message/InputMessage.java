package com.cards.controller.socket.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class InputMessage {
    private String senderUid;
    private String gameUid;
    private String cardUid;
    private MessageType type;
    private String detail;

    public enum MessageType{
        ADD_USER,
        REMOVE_USER,
        CHOOSE_OWN,
        CHOSE_BEST,
    }
}
