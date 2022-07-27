package HistoricalEventsBotApi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String link;
    private String date;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String text;
    private String img;

}
