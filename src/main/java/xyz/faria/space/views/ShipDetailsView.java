package xyz.faria.space.views;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import reactor.core.Disposable;
import xyz.faria.space.events.ShipBus;
import xyz.faria.space.events.ShipBusSubscriber;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.ShipRepository;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.views.components.ShipControlsCard;
import xyz.faria.space.views.components.ShipDetailCard;

@Route("/ships/:shipSymbol")
public class ShipDetailsView extends VerticalLayout implements BeforeEnterObserver,
    AfterNavigationObserver, ShipBusSubscriber {

    private final ShipRepository shipRepository;
    private final ShipService shipService;

    private String shipSymbol;

    private Disposable subscription;

    private Ship ship;


    public ShipDetailsView(ShipRepository shipRepository, ShipService shipService) {
        this.shipRepository = shipRepository;
        this.shipService = shipService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.shipSymbol = event.getRouteParameters().get("shipSymbol").orElse(null);
        this.subscription = ShipBus.getInstance().subscribe(this);
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
        System.out.println("Rendering ship details");
        this.removeAll();
        var detailsCard = new ShipDetailCard(ship);
        add(detailsCard);
        var controlsCard = new ShipControlsCard(ship, this.shipService);
        add(controlsCard);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        this.subscription.dispose();
    }

    @Override
    public boolean shouldReceiveUpdate(String shipSymbol) {
        return this.shipSymbol.equals(shipSymbol);
    }

    @Override
    public void onShipUpdate(Ship ship) {
        System.out.println("Ship updated event received");
        this.ship = ship;
        var ui = UI.getCurrent();
        ui.access(() -> {
            render();
            ui.push();
        });
    }
}
