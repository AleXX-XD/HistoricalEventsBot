package HistoricalEventsBotApi.service;

import HistoricalEventsBotApi.model.Event;
import HistoricalEventsBotApi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public Event getEvent(String link) {
        Optional<Event> opt = eventRepository.findById(link);
        return opt.orElse(null);
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }
}
