package com.github.jbence1994.erp.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageControllerTests {
    private final MessageController messageController = new MessageController();

    @Test
    void getMessageTest() {
        String result = messageController.getMessage().getBody();
        assertEquals("Hello World!", result);
    }
}
