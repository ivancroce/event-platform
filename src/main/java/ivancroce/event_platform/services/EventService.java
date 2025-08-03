package ivancroce.event_platform.services;

import ivancroce.event_platform.entities.Event;
import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.enums.Role;
import ivancroce.event_platform.exceptions.NotFoundException;
import ivancroce.event_platform.payloads.EventDTO;
import ivancroce.event_platform.repositories.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
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

        Event savedEvent = this.eventRepository.save(newEvent);

        log.info("The Event with ID '" + savedEvent.getId() + "' was created.");
        return savedEvent;
    }

    public Page<Event> findAllEvents(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventRepository.findAll(pageable);
    }

    public Event findById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with ID " + eventId + " not found."));
    }

    public Event updateEvent(UUID eventId, EventDTO payload, User currentUser) {
        Event foundEvent = this.findById(eventId);

        boolean isOwner = foundEvent.getOrganizer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (isOwner || isAdmin) {

            foundEvent.setTitle(payload.title());
            foundEvent.setDescription(payload.description());
            foundEvent.setEventDate(payload.eventDate());
            foundEvent.setLocation(payload.location());
            foundEvent.setTotalSeats(payload.totalSeats());

            return eventRepository.save(foundEvent);
        } else {
            throw new AuthorizationDeniedException("You are not authorized to modify this event.");
        }
    }

    public void deleteMyEvent(UUID eventId, User currentUser) {
        Event foundEvent = this.findById(eventId);

        boolean isOwner = foundEvent.getOrganizer().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (isOwner || isAdmin) {
            eventRepository.delete(foundEvent);
            log.info("Event with ID '" + eventId + "' was successfully cancelled.");
        } else {
            throw new AuthorizationDeniedException("You are not authorized to delete this event.");
        }
    }
}
