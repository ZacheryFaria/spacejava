package xyz.faria.space.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.model.SystemType;

@Getter
@Setter
@Entity
@Table(name = "system", indexes = {
    @Index(name = "idx_system_symbol", columnList = "symbol,reset_id", unique = true)})
public class System {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.annotation.Nonnull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @javax.annotation.Nonnull
    @Column(name = "sector_symbol", nullable = false)
    private String sectorSymbol;

    @javax.annotation.Nullable
    @Column(name = "constellation", nullable = true)
    private String constellation;

    @javax.annotation.Nullable
    @Column(name = "name", nullable = true)
    private String name;

    @javax.annotation.Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SystemType type;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @OneToMany
    @JoinColumn(name = "system_id")
    private List<Waypoint> waypoints;

    @ManyToOne
    @JoinColumn(name = "reset_id")
    private Reset reset;
}