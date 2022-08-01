package HistoricalEventsBotApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HistoricalEventsBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HistoricalEventsBotApplication.class, args);

    }
}
