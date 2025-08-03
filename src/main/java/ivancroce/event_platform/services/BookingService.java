package ivancroce.event_platform.services;

import ivancroce.event_platform.entities.Booking;
import ivancroce.event_platform.entities.Event;
import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.exceptions.BadRequestException;
import ivancroce.event_platform.repositories.BookingRepository;
import ivancroce.event_platform.repositories.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BookingService {
    @Autowired
    private EventService eventService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    public Booking createBooking(UUID eventId, User currentUser) {
        Event eventToBook = eventService.findById(eventId);

        if (eventToBook.getAvailableSeats() <= 0) {
            throw new BadRequestException("Event is sold out.");
        }

        if (bookingRepository.existsByUserAndEvent(currentUser, eventToBook)) {
            throw new BadRequestException("You have already booked this event.");
        }

        eventToBook.setAvailableSeats(eventToBook.getAvailableSeats() - 1);
        this.eventRepository.save(eventToBook);

        Booking newBooking = new Booking(currentUser, eventToBook);

        Booking savedBooking = this.bookingRepository.save(newBooking);

        log.info("New booking with ID " + savedBooking.getId() + " created for user " + currentUser.getUsername());

        return savedBooking;
    }

    public Page<Booking> findMyBookings(User currentUser, int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findByUser(currentUser, pageable);
    }
}
