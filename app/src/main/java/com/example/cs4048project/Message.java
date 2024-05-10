package com.example.cs4048project;

public class Message implements Comparable<Message> {
    private String senderId;

    private String recipientId;
    private String messageText;
    private long timestamp;

    public Message() {
    }
    public Message(String senderId, String recipientId, String messageText, long timestamp) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Message other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
}

