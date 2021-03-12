package com.cards.model;

import com.cards.controller.CardExtractor;
import com.cards.model.card.Card;

import java.util.Collections;
import java.util.Stack;

public class Deck <T extends Card> {
    private final Stack<T> cards;
    private final CardExtractor<T> extractor;

    public Deck(CardExtractor<T> extractor){
        this.extractor = extractor;
        cards = new Stack<>();
        refreshDeck();
    }

    private void refreshDeck(){
        extractor.extractCards(cards);
        Collections.shuffle(cards);
    }

    public T drawCard(){
        if (cards.empty()) {
            refreshDeck();
        }
        return cards.pop();
    }
}
