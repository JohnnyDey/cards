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
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class GameController {
    @Setter @Getter
    private Game game;

    public Player addPlayer(String playerUID, String username){
        return addPlayer(new Player(playerUID, username));
    }

    public Player addPlayer(Player player){
        if (player.getUsername() == null || player.getUid() == null)
            throw new IllegalArgumentException("Игрок должен иметь UID и имя");
        if (game.getPlayers().containsKey(player.getUid()))
            throw new IllegalArgumentException("Игрок с таким UID уже в игре");
        redrawCards(player);
        game.getOrder().add(player);
        return game.getPlayers().put(player.getUid(), player);
    }

    public void removePlayer(String playerUID){
        if (playerUID == null)
            throw new IllegalArgumentException("UID не должен быть null");
        if (!game.getPlayers().containsKey(playerUID))
            throw new IllegalArgumentException("Игрока UID=" + playerUID +" нет в игре");
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

    private void nextPlayer(){
        Queue<Player> order = game.getOrder();
        if (order.isEmpty()){
            order.addAll(game.getPlayers().values());
        }
        order.poll();
    }

    private void nextQuestion(){
        BlackCard question = game.getQuestionDeck().drawCard();
        game.setQuestion(question);
    }

    public Card answer(String userUid, String cardUid){
        Player player = game.getPlayers().get(userUid);
        if (player == null) throw new IllegalArgumentException("Игрок не найден");
        if (game.getAnswers().get(player) != null) throw new IllegalArgumentException("Игрок уже дал ответ");
        WhiteCard card = player.getCards().get(cardUid);
        if (card == null) throw new IllegalArgumentException("У игрока нет такой карты");
        player.getCards().remove(cardUid);
        game.getAnswers().put(player, card);
        return card;
    }

    public Player chooseWinner(String cardUid){
        AtomicReference<Player> winner = new AtomicReference<>();
        game.getAnswers().forEach((player, card) -> {
            if(cardUid.equals(card.getUid())){
                player.incrementScore();
                winner.set(player);
            }
        });
        if (winner.get() == null) throw new IllegalArgumentException("Игрока нет в списке участников");
        return winner.get();
    }

}
