package com.cards.database;

import com.cards.controller.CardExtractor;
import com.cards.controller.MockCardExtractor;
import com.cards.model.Deck;
import com.cards.model.Game;
import com.cards.model.card.BlackCard;
import com.cards.model.card.WhiteCard;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
@Scope("singleton")
public class CacheStorage {
    private Game game;

    CacheStorage(){
        game = new Game();
        Deck<BlackCard> qDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "q.txt", BlackCard.class));
        game.setQuestionDeck(qDeck);
        Deck<WhiteCard> aDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "a.txt", WhiteCard.class));
        game.setAnswersDeck(aDeck);
    }

    public Game getGameByUid(String uid){
        return game;
    }
}
