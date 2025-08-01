package ivancroce.event_platform.repositories;

import ivancroce.event_platform.entities.Booking;
import ivancroce.event_platform.entities.Event;
import ivancroce.event_platform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    boolean existsByUserAndEvent(User user, Event event);
}