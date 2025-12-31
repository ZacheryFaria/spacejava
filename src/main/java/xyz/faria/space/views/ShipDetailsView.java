package xyz.faria.space.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.ShipRepository;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.services.events.ShipUpdatedEvent;
import xyz.faria.space.views.components.ShipControlsCard;
import xyz.faria.space.views.components.ShipDetailCard;

@Route("/ships/:shipSymbol")
@Component
public class ShipDetailsView extends VerticalLayout implements BeforeEnterObserver,
    AfterNavigationObserver, ApplicationListener<ShipUpdatedEvent> {

    private final ShipRepository shipRepository;
    private final ShipService shipService;

    private String shipSymbol;

    private Ship ship;

    public ShipDetailsView(ShipRepository shipRepository, ShipService shipService) {
        this.shipRepository = shipRepository;
        this.shipService = shipService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.shipSymbol = event.getRouteParameters().get("shipSymbol").orElse(null);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        var shipData = shipRepository.findBySymbol(this.shipSymbol);
        if (shipData.isEmpty()) {
            System.err.println("Ship not found");
            UI.getCurrent().navigate("/ships");
            return;
        } else {
            this.ship = shipData.get();
        }
        render();
    }

    private void render() {
        this.removeAll();
        var detailsCard = new ShipDetailCard(ship);
        add(detailsCard);
        var controlsCard = new ShipControlsCard(ship, this.shipService);
        add(controlsCard);
    }

    @Override
    public void onApplicationEvent(ShipUpdatedEvent event) {
        this.ship = event.getShip();
        var ui = UI.getCurrent();
        ui.access(() -> {
            render();
            ui.push();
        });
    }
}
