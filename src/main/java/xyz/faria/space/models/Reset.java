package xyz.faria.space.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reset")
public class Reset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reset_date", nullable = false, unique = true)
    private OffsetDateTime resetDate;

    @Column(name = "systems_collected", nullable = false)
    private boolean systemsCollected = false;

    @Column(name = "systems_page", nullable = false)
    private Integer systemsPage = 1;
}
