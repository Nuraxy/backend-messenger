package com.xarun.backendmessenger.webSocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xarun.backendmessenger.user.User;
import com.xarun.backendmessenger.user.UserService;
import com.xarun.backendmessenger.webSocket.Message;
import com.xarun.backendmessenger.webSocket.MessageService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> webSocketSessions = new HashMap<>();
    private final UserService userService;
    private final MessageService messageService;

    public ChatWebSocketHandler(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage messagePackage) throws Exception {
        String json = messagePackage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(json, Message.class);

        if ("Greeting".equals(message.getMessageType())) {
            userService.greeting(message, session.getId());
            System.out.println("Geöffnet");
        } else {
            Message savedMessage = messageService.saveMessage(message);
            TextMessage savedTextMessage = new TextMessage(objectMapper.writeValueAsString(savedMessage));
            User receiver = userService.findById(message.getReceiverId());
            webSocketSessions.get(receiver.getSessionId()).sendMessage(savedTextMessage);
            System.out.println("Message");
        }
    }
// -----Groups----------------------------------------------------
//            for (WebSocketSession webSocketSession : webSocketSessions) {
//                if (webSocketSession.getId().equals(user.getSessionId()) || webSocketSession.getId().equals(session.getId())) {
//                    webSocketSession.sendMessage(messagePackage);
//                }
//            }
// -----Broadcast----------------------------------------------------
//        for(WebSocketSession webSocketSession : webSocketSessions){
//            webSocketSession.sendMessage(messagePackage);
//        }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        userService.removeSession(session.getId());
        webSocketSessions.remove(session.getId());
    }
}
