package com.cards.model;

import com.cards.model.card.WhiteCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Player {
    private final String uid;
    private final String username;
    @JsonIgnore
    private final Map<String, WhiteCard> cards = new HashMap<>();
    private int score = 0;

    public Player(String id, String username){
        this.uid = id;
        this.username = username;
    }

    public void incrementScore(){
        score++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return uid != null ? uid.equals(player.uid) : player.uid == null;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
