package com.xarun.backendmessenger.webSocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xarun.backendmessenger.user.User;
import com.xarun.backendmessenger.user.UserService;
import com.xarun.backendmessenger.webSocket.Message;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> webSocketSessions = new HashMap<>();
    private final UserService userService;

    public ChatWebSocketHandler(UserService userService) {
        this.userService = userService;
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

            System.out.println(session.getId());
            userService.greeting(message, session.getId());
            System.out.println(userService.findByName(message.getName()));
        } else {
            User userReceiver = userService.findById(message.getTo());
            webSocketSessions.get(userReceiver.getSessionId()).sendMessage(messagePackage);
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
