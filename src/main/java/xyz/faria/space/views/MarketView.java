package xyz.faria.space.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import xyz.faria.space.models.Waypoint;
import xyz.faria.space.services.WaypointService;
import xyz.faria.space.spaceapi.model.TradeSymbol;

@Route("/market")
public class MarketView extends VerticalLayout {

    private final WaypointService waypointService;

    private final Input systemInput = new Input();
    private final Input symbolInput = new Input();

    private final Div marketData = new Div();

    public MarketView(WaypointService waypointService) {
        this.waypointService = waypointService;

        var systemText = new Text("Enter System Symbol");
        var symbolText = new Text("Enter Trade Symbol to lookup");
        var button = new Button("Lookup", this::handleSubmit);

        add(systemText, systemInput, symbolText, symbolInput, button, marketData);
    }

    private void handleSubmit(ClickEvent<Button> buttonClickEvent) {
        this.marketData.removeAll();
        var tradeSymbol = TradeSymbol.valueOf(symbolInput.getValue());

        var waypoints = waypointService.findWaypointsBySystemAndHasMarketWithTradeSymbol(
            systemInput.getValue(), tradeSymbol);
        for (var waypoint : waypoints) {
            this.marketData.add(createMarketCard(waypoint));
        }
    }

    private Div createMarketCard(Waypoint waypoint) {
        return new Div(
            new Text(waypoint.getSymbol()),
            new Text(waypoint.getX() + ", " + waypoint.getY())
        );
    }


}
