package ivancroce.event_platform.controllers;

import ivancroce.event_platform.entities.Booking;
import ivancroce.event_platform.entities.User;
import ivancroce.event_platform.payloads.BookingRespDTO;
import ivancroce.event_platform.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public BookingRespDTO createBooking(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal User currentUser) {
        Booking newBooking = bookingService.createBooking(eventId, currentUser);
        return new BookingRespDTO(newBooking.getId());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    public Page<Booking> getMyBookings(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookingDate") String sortBy
    ) {
        return bookingService.findMyBookings(currentUser, page, size, sortBy);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public void deleteBooking(@PathVariable UUID bookingId, @AuthenticationPrincipal User currentUser) {
        bookingService.deleteMyBooking(bookingId, currentUser);
    }
}