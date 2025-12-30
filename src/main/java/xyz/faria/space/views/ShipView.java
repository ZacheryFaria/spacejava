package xyz.faria.space.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.ShipRepository;

@Route("/ships")
public class ShipView extends VerticalLayout {

    private final ShipRepository shipRepository;


    public ShipView(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;

        var ships = shipRepository.findAll();

        for (var ship : ships) {
            add(createShipCard(ship));
        }
    }

    private Card createShipCard(Ship ship) {
        Card card = new Card();
        var nav = ship.getNav();
        var title = new Div(new H2(ship.getSymbol()));
        var link = new Anchor(String.format("/ships/%s", ship.getSymbol()), "View ship details");
        card.setTitle(title);
        card.addToFooter(link);
        card.add(
            new Text(String.format("Waypoint: %s", nav.getWaypointSymbol()))
        );
        return card;
    }
}
