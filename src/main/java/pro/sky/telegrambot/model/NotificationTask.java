package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

 @Entity
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String messageContent;
    private LocalDateTime timeStamp;
    private boolean sentMessage;


    public NotificationTask(Long id, Long chatId, String messageContent, LocalDateTime timeStamp, boolean sentMessage) {
        this.id = id;
        this.chatId = chatId;
        this.messageContent = messageContent;
        this.timeStamp = timeStamp;
        this.sentMessage = sentMessage;
    }

    public NotificationTask(String messageContent, LocalDateTime timeStamp) {
        this.messageContent = messageContent;
        this.timeStamp = timeStamp;
    }

    public NotificationTask() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSentMessage() {
        return sentMessage;
    }

    public void setSentMessage(boolean sentMessage) {
        this.sentMessage = sentMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask task = (NotificationTask) o;
        return sentMessage == task.sentMessage && id.equals(task.id) && chatId.equals(task.chatId) && messageContent.equals(task.messageContent) && timeStamp.equals(task.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, messageContent, timeStamp, sentMessage);
    }

    public void markAsSent() {
        setSentMessage(true);
    }
}
