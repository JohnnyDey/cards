package com.cards.controller;

import com.cards.model.card.Card;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

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
            String data = new String(bdata, StandardCharsets.UTF_8);
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
