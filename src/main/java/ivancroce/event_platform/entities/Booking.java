package ivancroce.event_platform.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Booking {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, name = "booking date")
    private LocalDate bookingDate;

    public Booking(User user, Event event, LocalDate bookingDate) {
        this.user = user;
        this.event = event;
        this.bookingDate = LocalDate.now();
    }
}
