package com.cards.controller.messageresolver;

import com.cards.controller.socket.message.InputMessage;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Component
public class ResolverFactory {
    @Autowired
    private Map<String, Resolver> resolvers;

    public Resolver getResolver(InputMessage inputMessage) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Resolver resolver = resolvers.get(inputMessage.getType().toString());
        if (resolver == null) {
            throw new IllegalArgumentException("Неизвестный тип события");
        }
        Resolver copy = (Resolver) BeanUtils.cloneBean(resolver);
        copy.setInputMessage(inputMessage);
        return copy;
    }

    public Resolver getExceptionResolver(String msg, InputMessage inputMessage) {
        try {
            ExceptionResolver resolver = (ExceptionResolver) resolvers.get("EXCEPTION");
            Resolver copy = (Resolver) BeanUtils.cloneBean(resolver);
            copy.setInputMessage(inputMessage);
            resolver.setInputMessage(inputMessage);
            resolver.setExceptionDetail(msg);
            return resolver;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
