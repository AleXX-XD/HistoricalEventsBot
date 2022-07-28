package HistoricalEventsBotApi.repository;

import HistoricalEventsBotApi.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

    List<Event> findAllByDate(String date);
}
