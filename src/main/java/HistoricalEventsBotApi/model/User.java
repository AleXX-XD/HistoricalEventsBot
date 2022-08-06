package HistoricalEventsBotApi.model;

import HistoricalEventsBotApi.command.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


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
    private String name;
    @Column(name="is_admin")
    private boolean isAdmin;
    private Stage stage;
    @Column(name="stage_time")
    private LocalDateTime stageTime;
    @Column(name="current_events", columnDefinition = "TEXT")
    private String currentEvents;

    public User(String chatId) {
        this.chatId = chatId;
        active = true;
        stage = Stage.STAGE_NAME;
        stageTime = LocalDateTime.now();
        name = "Человек без имени";
        subscription = false;
        isAdmin = false;
    }

    public User() {}

    public void setStage(Stage stage) {
        this.stage = stage;
        stageTime = LocalDateTime.now();
    }
}
