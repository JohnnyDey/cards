package com.cards.controller;

import com.cards.model.card.Card;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import java.util.UUID;

public class MockCardExtractor <T extends Card>  extends CardExtractor<T>{
    private String txt;
    public MockCardExtractor(Class<T> clazz, String txt) {
        super(null, clazz);
        this.txt = txt;
    }

    private MockCardExtractor(String fileName, Class<T> clazz) {
        super(fileName, clazz);
    }

    @Override
    public void extractCards(Stack<T> ts) {
        try {
            for (int i = 0; i < 100; i++){
                T card = clazz.getConstructor().newInstance();
                card.setText(txt);
                card.setUid(UUID.randomUUID().toString());
                ts.push(card);
            }
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
