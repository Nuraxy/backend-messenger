package com.xarun.backendmessenger.webSocket;

public class Message {

    private String name;

    private String to;

    private String message;

    private Long keyE;

    private Long keyN;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getKeyE() {
        return keyE;
    }

    public void setKeyE(Long keyE) {
        this.keyE = keyE;
    }

    public Long getKeyN() {
        return keyN;
    }

    public void setKeyN(Long keyN) {
        this.keyN = keyN;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", keyE=" + keyE +
                ", keyN=" + keyN +
                '}';
    }
}
