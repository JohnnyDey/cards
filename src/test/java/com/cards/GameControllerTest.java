package com.cards;

import com.cards.controller.CardExtractor;
import com.cards.controller.GameController;
import com.cards.model.Deck;
import com.cards.model.Game;
import com.cards.model.Player;
import com.cards.model.card.BlackCard;
import com.cards.model.card.WhiteCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class GameControllerTest {
    @Autowired
    GameController controller;

    @BeforeEach
    void init() {
        Game game = new Game();
        Deck<BlackCard> qDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "q.txt", BlackCard.class));
        game.setQuestionDeck(qDeck);
        Deck<WhiteCard> aDeck = new Deck<>(new CardExtractor<>("decks" + File.separator + "a.txt", WhiteCard.class));
        game.setAnswersDeck(aDeck);
        game.setUid(UUID.randomUUID().toString());
        controller.setGame(game);
    }

    @Test
    void checkPlayerAdding() {
        String uid = UUID.randomUUID().toString();
        String playerName = "Player 1";
        controller.addPlayer(uid, playerName);
        Map<String, Player> players = controller.getGame().getPlayers();

        Assert.state(players.size() == 1, "Players count incorrect");
        Player player = players.get(uid);
        Assert.notNull(player, "Player not found by uid");
        Assert.state(player.getUsername().equals(playerName), "Player has incorrect name");
        Assert.state(player.getCards().size() == Game.CARD_LIMIT, "Player's cards count mismatched");
    }

    @Test
    void checkPlayerAddingWithoutUsername() {
        String uid = UUID.randomUUID().toString();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.addPlayer(uid, null), "Exception should be thrown");
    }

    @Test
    void checkPlayerAddingWithoutUID() {
        String playerName = "Player 1";
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.addPlayer(null, playerName), "Exception should be thrown");
    }

    @Test
    void checkPlayerAddingWithSameUID() {
        String uid = "1";
        controller.addPlayer(uid, "player 1");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.addPlayer(uid, "player 2"), "Exception should be thrown");
    }

    @Test
    void checkPlayerRemoving() {
        Player player1 = new Player(UUID.randomUUID().toString(), "Player 1");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player 2");
        controller.addPlayer(player1);
        controller.addPlayer(player2);

        controller.removePlayer(player1.getUid());

        Assert.isNull(controller.getGame().getPlayers().get(player1.getUid()), "Player was not deleted");
        Assert.notNull(controller.getGame().getPlayers().get(player2.getUid()), "Player was deleted");
    }

    @Test
    void checkNotExistedPlayerRemoving() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.removePlayer(UUID.randomUUID().toString()));
    }

    @Test
    void checkNullPlayerRemoving() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.removePlayer(null));
    }

    @Test
    void checkChooseAnswer() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);
        WhiteCard card = getFirstCard(player);
        controller.answer(uid, card.getUid());
        Map<Player, WhiteCard> answers = controller.getGame().getAnswers();

        Assert.state(answers.containsKey(player), "Player answer not found");
        Assert.notNull(answers.get(player), "Card not found in answers");
        Assert.state(answers.get(player).equals(card), "Card in answers incorrect");
        Assert.isNull(player.getCards().get(card.getUid()), "Player should remove card");
        Assert.state(player.getCards().size() == Game.CARD_LIMIT - 1, "Incorrect player card count");
    }

    @Test
    void checkChooseAnswerWrongCard() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.answer(uid, "fake uid"));
    }

    @Test
    void checkChooseAnswerTwice() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);
        controller.answer(uid, getFirstCard(player).getUid());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.answer(uid, getFirstCard(player).getUid()));
    }

    @Test
    void checkChooseAnswerWrongPlayer() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);
        WhiteCard card = getFirstCard(player);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.answer("fake uid", card.getUid()));
    }

    @Test
    void checkChooseWinner() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);
        WhiteCard card = getFirstCard(player);
        controller.answer(uid, card.getUid());
        controller.chooseWinner(card.getUid());

        Assert.state(player.getScore() == 1, "Score incorrect");
    }

    @Test
    void checkChooseWinnerWrongCard() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.chooseWinner("fake uid"));
    }

    @Test
    void checkEndRound() {
        Player player1 = new Player(UUID.randomUUID().toString(), "Player 1");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player 2");
        controller.addPlayer(player1);
        controller.addPlayer(player2);

        controller.endRound();

        Assert.state(controller.getGame().getLeader() == 1, "Wrong leader");
        Assert.notNull(controller.getGame().getQuestion(), "No question");
        Assert.isTrue(controller.getGame().getAnswers().isEmpty(), "Answers wasn't clear");
    }

    @Test
    void checkEndRoundManyTimes() {
        Player player1 = new Player(UUID.randomUUID().toString(), "Player 1");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player 2");
        controller.addPlayer(player1);
        controller.addPlayer(player2);

        BlackCard lastQuestion = null;
        for (int i = 1; i <= 10; i++) {
            WhiteCard player1Answer = getFirstCard(player1);
            WhiteCard player2Answer = getFirstCard(player2);

            controller.answer(player1.getUid(), player1Answer.getUid());
            controller.answer(player2.getUid(), player2Answer.getUid());
            controller.endRound();

            Assert.state(controller.getGame().getLeader() == i % 2, "Wrong leader");
            Assert.state(!controller.getGame().getQuestion().equals(lastQuestion), "Question is not changed");
            lastQuestion = controller.getGame().getQuestion();
            Assert.isNull(player1.getCards().get(player1Answer.getUid()), "Player should remove card");
            Assert.state(player1.getCards().size() == Game.CARD_LIMIT, "Player didn't draw card");
            Assert.isNull(player2.getCards().get(player2Answer.getUid()), "Player should remove card");
            Assert.state(player2.getCards().size() == Game.CARD_LIMIT, "Player didn't draw card");
        }
    }

    private WhiteCard getFirstCard(Player player){
        return (WhiteCard) player.getCards().values().toArray()[0];
    }
}
