package xyz.faria.space.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import lombok.NonNull;
import xyz.faria.space.components.ShipCard;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.spaceapi.client.ApiException;

public class ShipControlsCard extends ShipCard {

    @NonNull
    private final ShipService shipService;

    private Button dockButton;
    private Button orbitButton;

    public ShipControlsCard(@NonNull Ship ship, @NonNull ShipService shipService) {
        System.out.println("Creating ship controls card for ship " + ship.getSymbol());
        super(ship);
        this.shipService = shipService;
        this.addClassName("shipDetails");
    }

    private void handleOrbit(ClickEvent<Button> buttonClickEvent) {
        try {
            this.shipService.orbit(this.ship);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void handleDock(ClickEvent<Button> buttonClickEvent) {
        try {
            this.shipService.dock(this.ship);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        this.removeAll();
        var header = new Div(new H2("Ship controls"));
        setHeader(header);

        this.dockButton = new Button("Dock", this::handleDock);
        this.orbitButton = new Button("Orbit", this::handleOrbit);

        add(this.dockButton);
        add(this.orbitButton);
    }
}
