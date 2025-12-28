package xyz.faria.space.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import xyz.faria.space.services.AgentService;
import xyz.faria.space.spaceapi.Utils;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.model.FactionSymbol;

import java.util.Optional;

@Route("")
public class HomeView extends Composite<FormLayout> {

    public record NewAgentRecord(
            String agentSymbol
    ) {
    }

    private final AgentService agentService;
    private final Binder<NewAgentRecord> binder;


    public HomeView(AgentService agentService) {
        this.agentService = agentService;
        this.binder = new Binder<>(NewAgentRecord.class);

        var currentReset = Utils.getCurrentResetDate();

        System.out.println(currentReset);


        var agentSymbol = new TextField("Agent Symbol");
        var submitButton = new Button("Submit", this::submit);
        binder.forField(agentSymbol).asRequired("Agent symbol is required").bind("agentSymbol");

        var formLayout = getContent();
        formLayout.add(agentSymbol);
        formLayout.add(submitButton);
    }

    private void submit(ClickEvent<Button> buttonClickEvent) {
        var record = this.getFormDataObject();
        if (record.isPresent()) {
            try {
                var agent = agentService.registerAgent(record.get().agentSymbol(), FactionSymbol.COSMIC);
                System.out.println(agent.getId());
            } catch (ApiException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private Optional<NewAgentRecord> getFormDataObject() {
        try {
            return Optional.of(binder.writeRecord());
        } catch (ValidationException ex) {
            return Optional.empty();
        }
    }
}
