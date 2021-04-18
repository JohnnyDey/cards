package com.cards.model;

import com.cards.model.card.BlackCard;
import com.cards.model.card.WhiteCard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Getter @Setter
public class Game {
    public static final int PLAYER_LIMIT = 8;
    public static final int CARD_LIMIT = 8;

    private String uid;
    private Map<String, Player> players = new HashMap<>();
    private Queue<Player> order = new ArrayDeque<>();
    private int leader = 0;
    private BlackCard question;
    private Deck<BlackCard> questionDeck;
    private Deck<WhiteCard> answersDeck;
    private Map<Player, WhiteCard> answers = new HashMap<>();

}
