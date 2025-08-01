package ivancroce.event_platform.services;

import ivancroce.event_platform.entities.Event;
import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.payloads.EventDTO;
import ivancroce.event_platform.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventDTO payload, User organizer) {

        Event newEvent = new Event();
        newEvent.setTitle(payload.title());
        newEvent.setDescription(payload.description());
        newEvent.setEventDate(payload.eventDate());
        newEvent.setLocation(payload.location());
        newEvent.setTotalSeats(payload.totalSeats());
        newEvent.setAvailableSeats(payload.totalSeats());
        newEvent.setOrganizer(organizer);

        return eventRepository.save(newEvent);
    }
}
