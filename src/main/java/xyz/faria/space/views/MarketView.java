package xyz.faria.space.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import xyz.faria.space.repositories.WaypointRepository;
import xyz.faria.space.services.ResetService;
import xyz.faria.space.services.SystemService;
import xyz.faria.space.spaceapi.model.TradeSymbol;

@Route("/market")
public class MarketView extends VerticalLayout {

    private final WaypointRepository waypointRepository;

    private final Input systemInput = new Input();
    private final Input symbolInput = new Input();

    public MarketView(ResetService resetService, SystemService systemService,
        WaypointRepository waypointRepository) {
        this.waypointRepository = waypointRepository;

        var systemText = new Text("Enter System Symbol");
        var symbolText = new Text("Enter Trade Symbol to lookup");
        var button = new Button("Lookup", this::handleSubmit);

        add(systemText, systemInput, symbolText, symbolInput, button);
    }

    private void handleSubmit(ClickEvent<Button> buttonClickEvent) {
        System.out.println(systemInput.getValue());
        System.out.println(symbolInput.getValue());

        var tradeSymbol = TradeSymbol.valueOf(symbolInput.getValue());

        var markets = waypointRepository.findWaypointsBySystemAndHasMarketWithTradeSymbol(
            systemInput.getValue(), tradeSymbol);
        for (var market : markets) {
            System.out.println(market.getSymbol());
        }
    }


}
