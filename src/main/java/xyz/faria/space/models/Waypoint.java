package xyz.faria.space.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.model.WaypointFaction;
import xyz.faria.space.spaceapi.model.WaypointModifier;
import xyz.faria.space.spaceapi.model.WaypointOrbital;
import xyz.faria.space.spaceapi.model.WaypointTrait;
import xyz.faria.space.spaceapi.model.WaypointTraitSymbol;
import xyz.faria.space.spaceapi.model.WaypointType;

@Getter
@Setter
@Entity
@Table(name = "waypoint", indexes = {
    @Index(name = "idx_waypoint_has_been_scanned", columnList = "has_been_scanned")})
public class Waypoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.annotation.Nonnull
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @javax.annotation.Nonnull
    @Column(name = "type", nullable = false)
    private WaypointType type;

    @javax.annotation.Nonnull
    @Column(name = "system_symbol", nullable = false)
    private String systemSymbol;

    @javax.annotation.Nonnull
    @Column(name = "x", nullable = false)
    private Integer x;

    @javax.annotation.Nonnull
    @Column(name = "y", nullable = false)
    private Integer y;

    @javax.annotation.Nonnull
    @ElementCollection
    private List<WaypointOrbital> orbitals = new ArrayList<>();

    @javax.annotation.Nullable
    @Column(name = "orbits", nullable = true)
    private String orbits;

    @javax.annotation.Nullable
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "symbol", column = @Column(name = "faction_symbol")),
    })
    private WaypointFaction faction;

    @javax.annotation.Nonnull
    @ElementCollection(fetch = FetchType.EAGER)
    private List<WaypointTrait> traits = new ArrayList<>();

    @javax.annotation.Nullable
    @ElementCollection(fetch = FetchType.EAGER)
    private List<WaypointModifier> modifiers = new ArrayList<>();

    @javax.annotation.Nonnull
    @Column(name = "is_under_construction", nullable = false)
    private Boolean isUnderConstruction;

    /**
     * This column refers to whether this waypoint has individually been pulled by a player via the
     * /waypoint/{} endpoint only pulled via the /systems endpoint
     */
    @javax.annotation.Nonnull
    @Column(name = "has_been_scanned", nullable = false)
    private Boolean hasBeenScanned = false;

    @OneToOne(mappedBy = "waypoint")
    @javax.annotation.Nullable
    private Market market;

    @ManyToOne
    private xyz.faria.space.models.System system;

    public boolean hasMarketplace() {
        for (var trait : this.getTraits()) {
            if (trait.getSymbol().equals(WaypointTraitSymbol.MARKETPLACE)) {
                return true;
            }
        }
        return false;
    }
}