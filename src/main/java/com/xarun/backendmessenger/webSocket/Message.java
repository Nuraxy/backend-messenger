package com.xarun.backendmessenger.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

public class Message {

    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name="name")
    private String name;

    @Column(name="to")
    private String to;

    @Column(name="message")
    private String message;

    @Type(type = "json")
    @Column(name = "public_key", columnDefinition = "json")
    private JsonNode publicKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonNode getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(JsonNode publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", name='" + name + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", publicKey=" + publicKey +
                '}';
    }
}
