package com.cards.model;

import com.cards.controller.CardExtractor;
import com.cards.model.card.Card;

public class DeckMock <T extends Card> extends Deck<T> {

    public DeckMock(String mockVal) {
        super(null);
    }
    
    public DeckMock(CardExtractor<T> extractor) {
        super(extractor);
    }

    @Override
    public T drawCard() {
        return super.drawCard();
    }
}
