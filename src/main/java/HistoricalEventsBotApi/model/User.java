package HistoricalEventsBotApi.model;

import HistoricalEventsBotApi.command.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalTime;


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
    private LocalTime notificationTime;
    private String name;
    @Column(name="is_admin")
    private boolean isAdmin;
    private Stage stage;
    @Column(name="stage_time")
    private LocalTime stageTime;
    @Column(name="current_events", columnDefinition = "TEXT")
    private String currentEvents;

    public User(String chatId) {
        this.chatId = chatId;
        active = true;
        stage = Stage.STAGE_NAME;
        stageTime = LocalTime.now();
        subscription = false;
        isAdmin = false;
    }

    public User() {}

    public void setStage(Stage stage) {
        this.stage = stage;
        stageTime = LocalTime.now();
    }
}
