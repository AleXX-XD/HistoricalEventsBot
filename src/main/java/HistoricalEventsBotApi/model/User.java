package HistoricalEventsBotApi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;


@Getter
@Setter
@Entity
@Table(name="tg_user")
public class User {

    @Id
    @Column(name="chat_id")
    private String chatId;
    private boolean active;
    private boolean subscription;
    @Column(name="notification_time")
    private Time notificationTime;
    private String name;

    public User(String chatId, boolean active) {
        this.chatId = chatId;
        this.active = active;
        subscription = false;
    }

    public User() {

    }
}
