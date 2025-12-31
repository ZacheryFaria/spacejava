package xyz.faria.space.views.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import lombok.NonNull;
import xyz.faria.space.models.Ship;
import xyz.faria.space.services.ShipService;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.model.ShipNavStatus;

public class ShipControlsCard extends Card {

    @NonNull
    private final ShipService shipService;

    @NonNull
    private final Ship ship;

    public ShipControlsCard(@NonNull Ship ship, @NonNull ShipService shipService) {
        this.ship = ship;
        this.shipService = shipService;
        this.addClassName("shipDetails");
        var header = new Div(new H2("Ship controls"));
        setHeader(header);

        var dockButton = new Button("Dock", this::handleDock);
        dockButton.setEnabled(!ship.getNav().getStatus().equals(ShipNavStatus.DOCKED));
        var orbitButton = new Button("Orbit", this::handleOrbit);
        orbitButton.setEnabled(!ship.getNav().getStatus().equals(ShipNavStatus.IN_ORBIT));

        add(dockButton);
        add(orbitButton);
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
}
