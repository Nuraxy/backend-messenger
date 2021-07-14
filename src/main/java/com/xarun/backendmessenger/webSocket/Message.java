package com.xarun.backendmessenger.webSocket;

import javax.persistence.*;

public class Message {

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name="message_type")
    private String messageType;

    @Column(name="sender")
    private Long sender;

    @Column(name="receiver")
    private Long receiver;

    @Column(name="chat_id")
    private String chatId;

    @Column(name="message")
    private String message;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", messageType='" + messageType + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", chatId='" + chatId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
