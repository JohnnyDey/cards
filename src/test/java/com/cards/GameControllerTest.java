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
import org.springframework.data.util.Pair;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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
        Player player = new Player(UUID.randomUUID().toString(), "Player");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.endRound();
        Pair<Player, WhiteCard> pair = answerFirst(player, player2);

        Map<Player, WhiteCard> answers = controller.getGame().getAnswers();

        Assert.state(controller.getGame().getStatus().equals(Game.GameStatus.CHOOSING), "Game status incorrect");
        Assert.state(answers.containsKey(pair.getFirst()), "Player answer not found");
        Assert.notNull(answers.get(pair.getFirst()), "Card not found in answers");
        Assert.state(answers.get(pair.getFirst()).equals(pair.getSecond()), "Card in answers incorrect");
        Assert.isNull(pair.getFirst().getCards().get(pair.getSecond().getUid()), "Player should remove card");
        Assert.state(pair.getFirst().getCards().size() == Game.CARD_LIMIT - 1, "Incorrect player card count");
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
        Player player = new Player(UUID.randomUUID().toString(), "Player");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.endRound();
        answerFirst(player, player2);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> answerFirst(player, player2));
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
        Player player = new Player(UUID.randomUUID().toString(), "Player");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player");
        controller.addPlayer(player);
        controller.addPlayer(player2);
        controller.endRound();
        Pair<Player, WhiteCard> pair = answerFirst(player, player2);
        controller.chooseWinner(pair.getSecond().getUid(), controller.getLeader().getUid());

        Assert.state(pair.getFirst().getScore() == 1, "Score incorrect");
    }

    @Test
    void checkChooseWinnerWrongCard() {
        String uid = UUID.randomUUID().toString();
        Player player = new Player(uid, "Player");
        controller.addPlayer(player);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.chooseWinner("fake uid", controller.getLeader().getUid()));
    }

    @Test
    void checkEndRound() {
        Player player1 = new Player(UUID.randomUUID().toString(), "Player 1");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player 2");
        controller.addPlayer(player1);
        controller.addPlayer(player2);

        controller.endRound();

        Assert.state(controller.getGame().getStatus().equals(Game.GameStatus.ANSWERING), "null leader");
        Assert.notNull(controller.getGame().getOrder().peek(), "null leader");
        Assert.notNull(controller.getGame().getQuestion(), "No question");
        Assert.isTrue(controller.getGame().getAnswers().isEmpty(), "Answers wasn't clear");
    }

    @Test
    void checkEndRoundManyTimes() {
        Player player1 = new Player(UUID.randomUUID().toString(), "Player 1");
        Player player2 = new Player(UUID.randomUUID().toString(), "Player 2");
        controller.addPlayer(player1);
        controller.addPlayer(player2);
        controller.endRound();
        BlackCard lastQuestion = null;
        for (int i = 1; i <= 10; i++) {
            Pair<Player, WhiteCard> pair = answerFirst(player1, player2);

            controller.endRound();

            Assert.notNull(controller.getGame().getOrder().peek(), "Null leader");
            Assert.state(!controller.getGame().getQuestion().equals(lastQuestion), "Question is not changed");
            lastQuestion = controller.getGame().getQuestion();

            Assert.isNull(pair.getFirst().getCards().get(pair.getFirst().getUid()), "Player should remove card");
            Assert.state(pair.getFirst().getCards().size() == Game.CARD_LIMIT, "Player didn't draw card");
        }
    }

    private Pair<Player, WhiteCard> answerFirst(Player... players){
        return Arrays.stream(players).map(this::answerIfNotLeader).filter(Objects::nonNull).findFirst().get();
    }

    private Pair<Player, WhiteCard> answer(Player player) {
        WhiteCard answer = getFirstCard(player);
        controller.answer(player.getUid(), answer.getUid());
        return Pair.of(player, answer);
    }

    private Pair<Player, WhiteCard> answerIfNotLeader(Player player){
        if (!controller.getLeader().getUid().equals(player.getUid())){
            return answer(player);
        }
        return null;
    }

    private WhiteCard getFirstCard(Player player){
        return (WhiteCard) player.getCards().values().toArray()[0];
    }
}
