package xyz.faria.space.views;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {

    public HomeView() {
        add(new Anchor("/ships", "Ships"));
        add(new Anchor("/new-agent", "Create New Agent"));
    }
}
