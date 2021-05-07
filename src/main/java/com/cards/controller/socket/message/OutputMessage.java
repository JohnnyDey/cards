package com.cards.controller.socket.message;

import com.cards.model.Player;
import com.cards.model.card.Card;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutputMessage {
    private MessageType type;
    private Player[] players;
    private Card card;
    private Card[] cards;
    private String gameStatus;
    private String detail;

    public OutputMessage(MessageType type) {
        this.type = type;
    }

    public enum MessageType{
        PLAYER_LIST_UPDATED,
        NEW_ANSWER,
        NEXT_ROUND,
        EXCEPTION
    }
}
