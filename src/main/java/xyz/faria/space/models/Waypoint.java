package xyz.faria.space.models;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.model.WaypointFaction;
import xyz.faria.space.spaceapi.model.WaypointModifier;
import xyz.faria.space.spaceapi.model.WaypointOrbital;
import xyz.faria.space.spaceapi.model.WaypointTrait;
import xyz.faria.space.spaceapi.model.WaypointType;

@Getter
@Setter
@Entity
@Table(name = "waypoint")
public class Waypoint {

    public static final String SERIALIZED_NAME_SYMBOL = "symbol";
    public static final String SERIALIZED_NAME_TYPE = "type";
    public static final String SERIALIZED_NAME_SYSTEM_SYMBOL = "systemSymbol";
    public static final String SERIALIZED_NAME_X = "x";
    public static final String SERIALIZED_NAME_Y = "y";
    public static final String SERIALIZED_NAME_ORBITALS = "orbitals";
    public static final String SERIALIZED_NAME_ORBITS = "orbits";
    public static final String SERIALIZED_NAME_FACTION = "faction";
    public static final String SERIALIZED_NAME_TRAITS = "traits";
    public static final String SERIALIZED_NAME_MODIFIERS = "modifiers";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @SerializedName(SERIALIZED_NAME_SYMBOL)
    @javax.annotation.Nonnull
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @SerializedName(SERIALIZED_NAME_TYPE)
    @javax.annotation.Nonnull
    @Column(name = "type", nullable = false)
    private WaypointType type;
    @SerializedName(SERIALIZED_NAME_SYSTEM_SYMBOL)
    @javax.annotation.Nonnull
    @Column(name = "system_symbol", nullable = false)
    private String systemSymbol;
    @SerializedName(SERIALIZED_NAME_X)
    @javax.annotation.Nonnull
    @Column(name = "x", nullable = false)
    private Integer x;
    @SerializedName(SERIALIZED_NAME_Y)
    @javax.annotation.Nonnull
    @Column(name = "y", nullable = false)
    private Integer y;
    @SerializedName(SERIALIZED_NAME_ORBITALS)
    @javax.annotation.Nonnull
    @ElementCollection
    private List<WaypointOrbital> orbitals = new ArrayList<>();
    @SerializedName(SERIALIZED_NAME_ORBITS)
    @javax.annotation.Nullable
    @Column(name = "orbits", nullable = true)
    private String orbits;
    @SerializedName(SERIALIZED_NAME_FACTION)
    @javax.annotation.Nullable
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "symbol", column = @Column(name = "faction_symbol", nullable = false)),
    })
    private WaypointFaction faction;
    @SerializedName(SERIALIZED_NAME_TRAITS)
    @javax.annotation.Nonnull
    @ElementCollection
    private List<WaypointTrait> traits = new ArrayList<>();
    @SerializedName(SERIALIZED_NAME_MODIFIERS)
    @javax.annotation.Nullable
    @ElementCollection
    private List<WaypointModifier> modifiers = new ArrayList<>();

    @javax.annotation.Nonnull
    @Column(name = "is_under_construction", nullable = false)
    private Boolean isUnderConstruction;

}