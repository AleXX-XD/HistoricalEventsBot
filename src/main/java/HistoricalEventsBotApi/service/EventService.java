package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public long getCount() {
        return eventRepository.count();
    }

    public void saveAll(List<Event> eventList) {
        eventRepository.saveAll(eventList);
    }

    public List<Event> getEvents(String date) {
        return eventRepository.findAllByDate(date);
    }
}
