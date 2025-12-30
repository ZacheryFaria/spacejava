package xyz.faria.space.components;

import com.vaadin.flow.component.card.Card;
import lombok.Setter;
import xyz.faria.space.models.Ship;

@Setter
public abstract class ShipCard extends Card {

    protected Ship ship;

    public ShipCard(Ship ship) {
        System.out.println("Creating ship card for ship " + ship.getSymbol());
        this.ship = ship;
        render();
    }

    public abstract void render();

}
