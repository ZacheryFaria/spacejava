package xyz.faria.space.models;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import xyz.faria.space.spaceapi.api.FleetApi;
import xyz.faria.space.spaceapi.model.Cooldown;
import xyz.faria.space.spaceapi.model.ShipCargo;
import xyz.faria.space.spaceapi.model.ShipCrew;
import xyz.faria.space.spaceapi.model.ShipEngine;
import xyz.faria.space.spaceapi.model.ShipFrame;
import xyz.faria.space.spaceapi.model.ShipFuel;
import xyz.faria.space.spaceapi.model.ShipModule;
import xyz.faria.space.spaceapi.model.ShipMount;
import xyz.faria.space.spaceapi.model.ShipNav;
import xyz.faria.space.spaceapi.model.ShipReactor;
import xyz.faria.space.spaceapi.model.ShipRegistration;

/**
 * Ship details.
 */
@Getter
@Setter
@Entity
@Table(name = "ship")
public class Ship {

    public static final String SERIALIZED_NAME_SYMBOL = "symbol";
    @SerializedName(SERIALIZED_NAME_SYMBOL)
    @javax.annotation.Nonnull
    @Id
    @Column(name = "symbol", nullable = false)
    private String symbol;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public static final String SERIALIZED_NAME_REGISTRATION = "registration";
    @SerializedName(SERIALIZED_NAME_REGISTRATION)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "registration_name", nullable = false)),
        @AttributeOverride(name = "factionSymbol", column = @Column(name = "registration_faction_symbol", nullable = false)),
        @AttributeOverride(name = "role", column = @Column(name = "registration_role", nullable = false))
    })
    private ShipRegistration registration;

    public static final String SERIALIZED_NAME_NAV = "nav";
    @SerializedName(SERIALIZED_NAME_NAV)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "systemSymbol", column = @Column(name = "nav_system_symbol", nullable = false)),
        @AttributeOverride(name = "waypointSymbol", column = @Column(name = "nav_waypoint_symbol", nullable = false)),
        @AttributeOverride(name = "status", column = @Column(name = "nav_status", nullable = false)),
        @AttributeOverride(name = "flightMode", column = @Column(name = "nav_flight_mode", nullable = false)),
        @AttributeOverride(name = "route.destination.symbol", column = @Column(name = "nav_route_destination_symbol", nullable = false)),
        @AttributeOverride(name = "route.destination.type", column = @Column(name = "nav_route_destination_type", nullable = false)),
        @AttributeOverride(name = "route.destination.systemSymbol", column = @Column(name = "nav_route_destination_system_symbol", nullable = false)),
        @AttributeOverride(name = "route.destination.x", column = @Column(name = "nav_route_destination_x")),
        @AttributeOverride(name = "route.destination.y", column = @Column(name = "nav_route_destination_y")),
        @AttributeOverride(name = "route.origin.symbol", column = @Column(name = "nav_route_origin_symbol", nullable = false)),
        @AttributeOverride(name = "route.origin.type", column = @Column(name = "nav_route_origin_type", nullable = false)),
        @AttributeOverride(name = "route.origin.systemSymbol", column = @Column(name = "nav_route_origin_system_symbol", nullable = false)),
        @AttributeOverride(name = "route.origin.x", column = @Column(name = "nav_route_origin_x")),
        @AttributeOverride(name = "route.origin.y", column = @Column(name = "nav_route_origin_y")),
        @AttributeOverride(name = "route.departureTime", column = @Column(name = "nav_route_departure_time", nullable = false)),
        @AttributeOverride(name = "route.arrival", column = @Column(name = "nav_route_arrival", nullable = false))
    })
    private ShipNav nav;

    public static final String SERIALIZED_NAME_CREW = "crew";
    @SerializedName(SERIALIZED_NAME_CREW)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "current", column = @Column(name = "crew_current", nullable = false)),
        @AttributeOverride(name = "required", column = @Column(name = "crew_required", nullable = false)),
        @AttributeOverride(name = "capacity", column = @Column(name = "crew_capacity", nullable = false)),
        @AttributeOverride(name = "rotation", column = @Column(name = "crew_rotation", nullable = false)),
        @AttributeOverride(name = "morale", column = @Column(name = "crew_morale", nullable = false)),
        @AttributeOverride(name = "wages", column = @Column(name = "crew_wages", nullable = false))
    })
    private ShipCrew crew;

    public static final String SERIALIZED_NAME_FRAME = "frame";
    @SerializedName(SERIALIZED_NAME_FRAME)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "symbol", column = @Column(name = "frame_symbol", nullable = false)),
        @AttributeOverride(name = "name", column = @Column(name = "frame_name", nullable = false)),
        @AttributeOverride(name = "description", column = @Column(name = "frame_description", nullable = false)),
        @AttributeOverride(name = "moduleSlots", column = @Column(name = "frame_module_slots", nullable = false)),
        @AttributeOverride(name = "mountingPoints", column = @Column(name = "frame_mounting_points", nullable = false)),
        @AttributeOverride(name = "fuelCapacity", column = @Column(name = "frame_fuel_capacity", nullable = false)),
        @AttributeOverride(name = "condition", column = @Column(name = "frame_condition", nullable = false)),
        @AttributeOverride(name = "quality", column = @Column(name = "frame_quality", nullable = false)),
        @AttributeOverride(name = "integrity", column = @Column(name = "frame_integrity", nullable = false)),
        @AttributeOverride(name = "requirements.power", column = @Column(name = "frame_req_power")),
        @AttributeOverride(name = "requirements.crew", column = @Column(name = "frame_req_crew")),
        @AttributeOverride(name = "requirements.slots", column = @Column(name = "frame_req_slots"))
    })
    private ShipFrame frame;

    public static final String SERIALIZED_NAME_REACTOR = "reactor";
    @SerializedName(SERIALIZED_NAME_REACTOR)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "symbol", column = @Column(name = "reactor_symbol", nullable = false)),
        @AttributeOverride(name = "name", column = @Column(name = "reactor_name", nullable = false)),
        @AttributeOverride(name = "description", column = @Column(name = "reactor_description", nullable = false)),
        @AttributeOverride(name = "condition", column = @Column(name = "reactor_condition", nullable = false)),
        @AttributeOverride(name = "integrity", column = @Column(name = "reactor_integrity", nullable = false)),
        @AttributeOverride(name = "powerOutput", column = @Column(name = "reactor_power_output", nullable = false)),
        @AttributeOverride(name = "quality", column = @Column(name = "reactor_quality", nullable = false)),
        @AttributeOverride(name = "requirements.power", column = @Column(name = "reactor_req_power")),
        @AttributeOverride(name = "requirements.crew", column = @Column(name = "reactor_req_crew")),
        @AttributeOverride(name = "requirements.slots", column = @Column(name = "reactor_req_slots"))
    })
    private ShipReactor reactor;

    public static final String SERIALIZED_NAME_ENGINE = "engine";
    @SerializedName(SERIALIZED_NAME_ENGINE)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "symbol", column = @Column(name = "engine_symbol", nullable = false)),
        @AttributeOverride(name = "name", column = @Column(name = "engine_name", nullable = false)),
        @AttributeOverride(name = "description", column = @Column(name = "engine_description", nullable = false)),
        @AttributeOverride(name = "condition", column = @Column(name = "engine_condition", nullable = false)),
        @AttributeOverride(name = "integrity", column = @Column(name = "engine_integrity", nullable = false)),
        @AttributeOverride(name = "speed", column = @Column(name = "engine_speed", nullable = false)),
        @AttributeOverride(name = "quality", column = @Column(name = "engine_quality", nullable = false)),
        @AttributeOverride(name = "requirements.power", column = @Column(name = "engine_req_power")),
        @AttributeOverride(name = "requirements.crew", column = @Column(name = "engine_req_crew")),
        @AttributeOverride(name = "requirements.slots", column = @Column(name = "engine_req_slots"))
    })
    private ShipEngine engine;

    public static final String SERIALIZED_NAME_COOLDOWN = "cooldown";
    @SerializedName(SERIALIZED_NAME_COOLDOWN)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "shipSymbol", column = @Column(name = "cooldown_ship_symbol", nullable = false)),
        @AttributeOverride(name = "totalSeconds", column = @Column(name = "cooldown_total_seconds", nullable = false)),
        @AttributeOverride(name = "remainingSeconds", column = @Column(name = "cooldown_remaining_seconds", nullable = false)),
        @AttributeOverride(name = "expiration", column = @Column(name = "cooldown_expiration"))
    })
    private Cooldown cooldown;

    @javax.annotation.Nonnull
    @ElementCollection
    @CollectionTable(name = "ship_modules", joinColumns = @JoinColumn(name = "ship_symbol"))
    private List<ShipModule> modules = new ArrayList<>();

    @javax.annotation.Nonnull
    @ElementCollection
    @CollectionTable(name = "ship_mounts", joinColumns = @JoinColumn(name = "ship_symbol"))
    private List<ShipMount> mounts = new ArrayList<>();

    public static final String SERIALIZED_NAME_CARGO = "cargo";
    @SerializedName(SERIALIZED_NAME_CARGO)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "capacity", column = @Column(name = "cargo_capacity", nullable = false)),
        @AttributeOverride(name = "units", column = @Column(name = "cargo_units", nullable = false))
    })
    private ShipCargo cargo;

    public static final String SERIALIZED_NAME_FUEL = "fuel";
    @SerializedName(SERIALIZED_NAME_FUEL)
    @javax.annotation.Nonnull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "current", column = @Column(name = "fuel_current", nullable = false)),
        @AttributeOverride(name = "capacity", column = @Column(name = "fuel_capacity", nullable = false)),
        @AttributeOverride(name = "consumed.amount", column = @Column(name = "fuel_consumed_amount", nullable = false)),
        @AttributeOverride(name = "consumed.timestamp", column = @Column(name = "fuel_consumed_timestamp", nullable = false))
    })
    private ShipFuel fuel;

    public FleetApi getFleetApi() {
        return new FleetApi(this.getAgent().getAgentClient());
    }

}
