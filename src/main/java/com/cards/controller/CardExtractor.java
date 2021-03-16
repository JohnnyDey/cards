package com.cards.controller;

import com.cards.model.card.Card;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
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
            String filename = ResourceUtils.getFile(fileName).getAbsolutePath();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                while (reader.ready()){
                    T card = clazz.getConstructor().newInstance();
                    card.setText(reader.readLine());
                    card.setUid(UUID.randomUUID().toString());
                    ts.push(card);
                }
            } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
