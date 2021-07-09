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

    @Column(name="message_to_sender")
    private String messageToSender;

    @Column(name="receiver")
    private Long receiver;

    @Column(name="message_to_receiver")
    private String messageToReceiver;


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

    public String getMessageToSender() {
        return messageToSender;
    }

    public void setMessageToSender(String messageToSender) {
        this.messageToSender = messageToSender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getMessageToReceiver() {
        return messageToReceiver;
    }

    public void setMessageToReceiver(String messageToReceiver) {
        this.messageToReceiver = messageToReceiver;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", messageType='" + messageType + '\'' +
                ", sender=" + sender +
                ", messageToSender='" + messageToSender + '\'' +
                ", receiver=" + receiver +
                ", messageToReceiver='" + messageToReceiver + '\'' +
                '}';
    }
}
