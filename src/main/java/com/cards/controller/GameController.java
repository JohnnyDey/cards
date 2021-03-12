package com.cards.controller;

import com.cards.model.Game;
import com.cards.model.Player;
import com.cards.model.card.BlackCard;
import com.cards.model.card.Card;
import com.cards.model.card.WhiteCard;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GameController {
    @Setter @Getter
    private Game game;

    public Player addPlayer(String playerUID){
        return addPlayer(new Player(playerUID));
    }

    public Player addPlayer(Player player){
        redrawCards(player);
        return game.getPlayers().put(player.getUid(), player);
    }

    public void removePlayer(String playerUID){
        game.getPlayers().remove(playerUID);
    }

    private void redrawCards(){
        game.getPlayers().forEach((id, player) -> redrawCards(player));
    }

    private void redrawCards(Player player){
        Map<String, WhiteCard> cards = player.getCards();
        while (cards.size() < Game.CARD_LIMIT) {
            WhiteCard card = game.getAnswersDeck().drawCard();
            cards.put(card.getUid(), card);
        }
    }

    public void endRound(){
        nextPlayer();
        nextQuestion();
        game.getAnswers().clear();
        redrawCards();
    }

    public void nextPlayer(){
        int leader = game.getLeader() + 1;
        if(leader >= game.getPlayers().size()){
            leader = 0;
        }
        game.setLeader(leader);
    }

    public void nextQuestion(){
        BlackCard question = game.getQuestionDeck().drawCard();
        game.setQuestion(question);
    }

    public Card answer(String userUid, String cardUid){
        Player player = game.getPlayers().get(userUid);
        if (player == null) throw new IllegalArgumentException("No player found");
        WhiteCard card = player.getCards().get(cardUid);
        if (card == null) throw new IllegalArgumentException("No card found");
        game.getAnswers().put(player, card);
        return card;
    }

    public void chooseWinner(String cardUid){
        game.getAnswers().forEach((key, value) -> {
            if(cardUid.equals(value.getUid())){
                key.incrementScore();
            }
        });
        game.getAnswers().clear();
    }

}
