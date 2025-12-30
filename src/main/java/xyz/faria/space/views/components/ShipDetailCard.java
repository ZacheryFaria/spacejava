package xyz.faria.space.views.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import xyz.faria.space.components.DataRow;
import xyz.faria.space.components.ShipCard;
import xyz.faria.space.models.Ship;

public class ShipDetailCard extends ShipCard {

    public ShipDetailCard(Ship ship) {
        super(ship);
        render();
    }

    public void render() {
        this.removeAll();
        var header = new Div(new H2("Ship details"));
        setHeader(header);
        setHeaderPrefix(new Anchor("/ships", "Back"));

        add(new DataRow("Ship symbol", this.ship.getSymbol()));
        add(new DataRow("Ship Location", this.ship.getNav().getWaypointSymbol()));
        add(new DataRow("Ship Status", this.ship.getNav().getStatus()));
        add(new DataRow("Ship Fuel", this.ship.getFuel()));
        add(new Div("--- DESTINATION ---"));
        add(
            new DataRow("Destination", this.ship.getNav().getRoute().getDestination().getSymbol()));
        add(new DataRow("Arrival", this.ship.getNav().getRoute().getArrival()));
        add(new Div("--- ORIGIN ---"));
        add(
            new DataRow("Origin", this.ship.getNav().getRoute().getOrigin().getSymbol()));
        add(new DataRow("Departure", this.ship.getNav().getRoute().getDepartureTime()));

        add(new Div("--- CARGO ---"));
        add(new DataRow("Capacity", this.ship.getCargo().toCapacityString()));

        if (!this.ship.getCargo().getInventory().isEmpty()) {
            add(new Div("- inventory -"));
            for (var c : this.ship.getCargo().getInventory()) {
                add(new DataRow(c.getSymbol().toString(), c.getUnits()));
            }
        }
    }
}
