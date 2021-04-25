package com.xarun.backendmessenger.webSocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xarun.backendmessenger.user.User;
import com.xarun.backendmessenger.user.UserService;
import com.xarun.backendmessenger.webSocket.Message;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private final UserService userService;

    public ChatWebSocketHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){

        int n = 2773;
        int e = 17;
        int kl = 0;
        // Hier versuche ich Arbeitsaufwand (Rechnenzeit) zu sparen in dem ich
        // mir die kleinste Zahl suche.
        kl = (n > e) ? e : n;
        // Der GGT wird hier berechnet.
        for (int i = kl; i > 1; i--) {
            if ((n % i) == 0 && (e % i) == 0) {
                System.out.println("kleinsterTeiler" + i);
            }
        }
        // teilerfremde Zahlen haben immer den Teiler 1
        System.out.println("keinen gefunden für " + e);

        String test = "ASCIIÄÖäö";
        final byte[] bytes = test.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println(Arrays.toString(bytes));
        System.out.println(new String(bytes, StandardCharsets.ISO_8859_1));
        for(int i = 0; i < test.length(); i++) {
            System.out.println("Value of "+ test.charAt(i) + " is " + (int) test.charAt(i));
        }


        webSocketSessions.add(session);
        System.out.println("SocketSessions: " + webSocketSessions);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage messagePackage) throws Exception{
        System.out.println("Incoming Message: " + messagePackage + " Message Payload: " + messagePackage.getPayload());
        String json = messagePackage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(json, Message.class);
        if(message.getTo().equals("Greeting")){
            System.out.println(session.getId());
            User user = userService.findByName(message.getName());
            userService.greeting(user, session.getId(), message);
            System.out.println(userService.findByName(message.getName()));
        }else {
            User user = userService.findByName(message.getTo());
            // todo findBySessionId without for(...) [change to Map instead ArrayList then search for user id to find a session or a list of sessions]
            for (WebSocketSession webSocketSession : webSocketSessions) {
                if (webSocketSession.getId().equals(user.getSessionId()) || webSocketSession.getId().equals(session.getId())){
                    webSocketSession.sendMessage(messagePackage);
                }
            }
        }
// -----Broadcast----------------------------------------------------
//        for(WebSocketSession webSocketSession : webSocketSessions){
//            webSocketSession.sendMessage(messagePackage);
//        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        webSocketSessions.remove(session);
        //todo delete session from user
    }
}
