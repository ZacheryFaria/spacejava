package xyz.faria.space.views.components;

import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import xyz.faria.space.components.DataRow;
import xyz.faria.space.models.Ship;

public class ShipDetailCard extends Card {

    public ShipDetailCard(Ship ship) {
        var header = new Div(new H2("Ship details"));
        setHeader(header);
        setHeaderPrefix(new Anchor("/ships", "Back"));

        add(new DataRow("Ship symbol", ship.getSymbol()));
        add(new DataRow("Ship Location", ship.getNav().getWaypointSymbol()));
        add(new DataRow("Ship Status", ship.getNav().getStatus()));
        add(new DataRow("Ship Fuel", ship.getFuel()));
        add(new Div("--- DESTINATION ---"));
        add(
            new DataRow("Destination", ship.getNav().getRoute().getDestination().getSymbol()));
        add(new DataRow("Arrival", ship.getNav().getRoute().getArrival()));
        add(new Div("--- ORIGIN ---"));
        add(
            new DataRow("Origin", ship.getNav().getRoute().getOrigin().getSymbol()));
        add(new DataRow("Departure", ship.getNav().getRoute().getDepartureTime()));

        add(new Div("--- CARGO ---"));
        add(new DataRow("Capacity", ship.getCargo().toCapacityString()));

        if (!ship.getCargo().getInventory().isEmpty()) {
            add(new Div("- inventory -"));
            for (var c : ship.getCargo().getInventory()) {
                add(new DataRow(c.getSymbol().toString(), c.getUnits()));
            }
        }
    }
}
