package com.cards.database;

import com.cards.controller.CardExtractor;
import com.cards.model.Deck;
import com.cards.model.Game;
import com.cards.model.card.BlackCard;
import com.cards.model.card.WhiteCard;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Scope("singleton")
public class CacheStorage {
    private final List<Game> games = new ArrayList<>();

    CacheStorage(){
        createGame();
    }

    public Game getGameByUid(String uid){
        //todo
        return games.get(0);
    }

    public Game createGame() {
        Game game = new Game();
        Deck<BlackCard> qDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "q.txt", BlackCard.class));
        game.setQuestionDeck(qDeck);
        Deck<WhiteCard> aDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "a.txt", WhiteCard.class));
        game.setAnswersDeck(aDeck);
        game.setUid(UUID.randomUUID().toString());
        games.add(game);
        //todo: убрать фейк
        BlackCard question = game.getQuestionDeck().drawCard();
        game.setQuestion(question);
        return game;
    }

    public List<Game> getPublicGames(){
        return games;
    }
}
