package com.cards.controller;

import com.cards.model.card.Card;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.UUID;

public class CardExtractor<T extends Card> {
    private final String fileName;
    protected final Class<T> clazz;

    public CardExtractor(String fileName, Class<T> clazz){
        this.fileName = fileName;
        this.clazz = clazz;
    }

    public void extractCards(Stack<T> ts) {
        try {
            ClassPathResource cpr = new ClassPathResource(this.fileName);
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String[] words = new String(bdata, StandardCharsets.UTF_8).split("\n");
            for (String word : words) {
                T card = clazz.getConstructor().newInstance();
                card.setText(word.trim());
                card.setUid(UUID.randomUUID().toString());
                ts.push(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
