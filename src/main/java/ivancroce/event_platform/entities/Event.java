package ivancroce.event_platform.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false, name = "event_date")
    private LocalDate eventDate;

    @Column(nullable = false)
    private String location;


    @Column(nullable = false, name = "total_seats")
    private int totalSeats;

    @Column(nullable = false, name = "available_seats")
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    public Event(String title, String description, LocalDate eventDate, String location, int totalSeats, int availableSeats, User organizer) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.organizer = organizer;
    }
}