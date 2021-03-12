package com.cards.controller.socket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputMessage {
    private String senderUid;
    private String playerUid;
    private String gameUid;
    private String cardUid;
    private MessageType type;

    public InputMessage() {
    }

    public InputMessage(String senderUid, MessageType type) {
        this.senderUid = senderUid;
        this.type = type;
    }

    public enum MessageType{
        ADD_USER,
        REMOVE_USER,
        CHOOSE_OWN,
        CHOSE_BEST,
    }
}
