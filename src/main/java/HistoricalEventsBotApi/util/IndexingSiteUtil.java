package HistoricalEventsBotApi.util;

import HistoricalEventsBotApi.service.EventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class IndexingSiteUtil {

    @Value("${siteconfig.url}")
    private String url;

    private final Logger log = Logger.getLogger(IndexingSiteUtil.class);
    private final EventService eventService;
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    public IndexingSiteUtil(EventService eventService) {
        this.eventService = eventService;
    }

    public void startIndexing() {
        try {
            log.info("Запущена индексация сайта '" + url + "'");
            long startTime = System.currentTimeMillis();
            LocalDate date = LocalDate.of(2020,1,1);
            List<Future<Object>> futures = new ArrayList<>();

            while(date.isBefore(LocalDate.of(2021,1,1))) {
                String path = "/date/" + date.getMonthValue() + "/" + date.getDayOfMonth();
                ParseSiteUtil parseSite = new ParseSiteUtil(url, path, eventService);
                futures.add(executor.submit(parseSite,(Object)null));
                date = date.plusDays(1);
            }
            for (Future<Object> future : futures) {
                future.get();
            }

            log.info("Индексация сайта закончена.\n" +
                    "Добавлено: " + eventService.getCount() + " статей\n" +
                    "Время = " + (System.currentTimeMillis() - startTime)/1000 + " сек.");
        }
        catch (Exception ex) {
            log.warn("Ошибка при индексации сайта: " + ex.getMessage());
        }
    }
}
