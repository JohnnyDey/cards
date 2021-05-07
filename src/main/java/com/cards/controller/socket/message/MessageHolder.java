package com.cards.controller.socket.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class MessageHolder {
    @Getter @Setter
    private InputMessage message;
}
