package HistoricalEventsBotApi.util;

import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.service.EventService;
import org.jboss.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParseSiteUtil implements Runnable {

    private final Logger log = Logger.getLogger(ParseSiteUtil.class);
    private final String url;
    private final String path;
    private final EventService eventService;
    private final List<Event> eventList = new ArrayList<>();

    public ParseSiteUtil(String url, String path, EventService eventService) {
        this.url = url;
        this.path = path;
        this.eventService = eventService;
    }

    @Override
    public void run() {
        parsePage(path);
    }

    protected void parsePage(String path) {
        try {
            Document doc = getHtml(url + path);
            Elements links = doc.select("a[href~=^" + path + "]");
            for(Element link: links) {
                String newLink = url + link.attributes().get("href");
                indexPage(newLink, path);
                eventService.saveAll(eventList);
            }
        } catch (Exception iex) {
            log.warn("Ошибка при индексации. Путь : '" + url + path + "' / " + iex.getMessage());
        }
    }

    private Document getHtml(String path) throws InterruptedException, IOException {
        Random random = new Random();
        String numberUserAgent = String.valueOf(random.nextInt());
        Thread.sleep(150);
        Connection.Response response = Jsoup.connect(path)
                .userAgent("SearchAgent4000" + numberUserAgent)
                .referrer("https://www.google.com/")
                .timeout(1000000).maxBodySize(0).ignoreHttpErrors(true).followRedirects(true).execute();
        return response.parse();
    }

    private void indexPage(String link, String path) {
        try {
            Document doc = getHtml(link);
            Event event = new Event();
            event.setLink(link);
            event.setDate(path.replaceAll("/date/", ""));
            event.setTitle(doc.select("h1.page-header").get(0).text());

            try {
                String imgLink = doc.select("div.field-images").get(0)
                        .select("img.img-responsive").get(0).attributes().get("src");
                event.setImg(imgLink);
            }
            catch (Exception ex) {
                event.setImg(null);
            }

            StringBuilder builder = new StringBuilder();
            builder.append(" ");
            for(Element el : doc.select("div.field-body").select("p")) {
                builder.append(el.text().trim()).append("\n");
            }
            if(builder.toString().trim().length() != 0) {
                event.setText(builder.toString());
                eventList.add(event);
            }
        }
        catch (Exception ex) {
            log.warn("Ошибка при индексации статьи '" + link + "' / " + ex.getMessage());
        }
    }
}
