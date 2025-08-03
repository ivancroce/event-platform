package ivancroce.event_platform.controllers;

import ivancroce.event_platform.entities.Event;
import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.exceptions.ValidationException;
import ivancroce.event_platform.payloads.EventDTO;
import ivancroce.event_platform.payloads.EventRespDTO;
import ivancroce.event_platform.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public EventRespDTO createEvent(@RequestBody @Validated EventDTO payload,
                                    BindingResult validationResult,
                                    @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            Event newEvent = this.eventService.createEvent(payload, currentUser);
            return new EventRespDTO(newEvent.getId());
        }
    }

    @GetMapping
    public Page<Event> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDate") String sortBy) {
        return eventService.findAllEvents(page, size, sortBy);
    }

    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable UUID eventId) {
        return eventService.findById(eventId);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public Event updateEvent(@PathVariable UUID eventId,
                             @RequestBody @Validated EventDTO payload,
                             @AuthenticationPrincipal User currentUser) {
        return eventService.updateEvent(eventId, payload, currentUser);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ORGANIZER', 'ADMIN')")
    public void deleteEvent(@PathVariable UUID eventId,
                            @AuthenticationPrincipal User currentUser) {
        eventService.deleteMyEvent(eventId, currentUser);
    }
}
