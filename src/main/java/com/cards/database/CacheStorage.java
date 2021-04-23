package com.cards.database;

import com.cards.controller.CardExtractor;
import com.cards.model.Deck;
import com.cards.model.Game;
import com.cards.model.card.BlackCard;
import com.cards.model.card.WhiteCard;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

@Repository
@Scope("singleton")
public class CacheStorage {
    private final Map<String, Game> games = new HashMap<>();

    CacheStorage(){
        createGame();
    }

    public Game getGameByUid(String uid){
        return games.get(uid);
    }

    public Game getGameByUserUid(String uid){
        return games.values().stream()
                .filter(game -> game.getPlayers().containsKey(uid))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Game createGame() {
        Game game = new Game();
        Deck<BlackCard> qDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "q.txt", BlackCard.class));
        game.setQuestionDeck(qDeck);
        Deck<WhiteCard> aDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "a.txt", WhiteCard.class));
        game.setAnswersDeck(aDeck);
        game.setUid(UUID.randomUUID().toString());
        games.put(game.getUid(), game);
        //todo: убрать фейк
        BlackCard question = game.getQuestionDeck().drawCard();
        game.setQuestion(question);
        game.setStatus(Game.GameStatus.ANSWERING);
        return game;
    }

}
