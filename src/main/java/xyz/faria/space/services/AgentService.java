package xyz.faria.space.services;

import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Contract;
import xyz.faria.space.models.Ship;
import xyz.faria.space.repositories.AgentRepository;
import xyz.faria.space.repositories.ContractRepository;
import xyz.faria.space.repositories.ShipRepository;
import xyz.faria.space.spaceapi.api.AgentsApi;
import xyz.faria.space.spaceapi.api.GlobalApi;
import xyz.faria.space.spaceapi.client.ApiClient;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.converters.AgentConverter;
import xyz.faria.space.spaceapi.converters.ContractConverter;
import xyz.faria.space.spaceapi.converters.ShipConverter;
import xyz.faria.space.spaceapi.model.FactionSymbol;
import xyz.faria.space.spaceapi.model.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final ContractRepository contractRepository;
    private final ShipRepository shipRepository;
    private final ResetService resetService;


    public @Nullable Agent getAgentById(UUID id) {
        return agentRepository.findById(id).orElse(null);
    }

    public Agent registerAgent(String symbol, FactionSymbol faction) throws ApiException {
        ApiClient apiClient = ApiClient.getAccountApiClient();
        GlobalApi globalApi = new GlobalApi(apiClient);
        RegisterRequest request = new RegisterRequest();
        request.setSymbol(symbol);
        request.setFaction(faction);
        var response = globalApi.register(request);
        Agent agent = AgentConverter.fromApiAgent(response.getData().getAgent());
        agent.setToken(response.getData().getToken());
        agent.setReset(resetService.getCurrentReset());

        Contract contract = ContractConverter.fromApiContract(response.getData().getContract());
        contract.setAgent(agent);

        List<Ship> newShipModels = new ArrayList<>();
        for (var ship : response.getData().getShips()) {
            var shipModel = ShipConverter.fromApiShip(ship);
            shipModel.setAgent(agent);
            newShipModels.add(shipModel);
        }

        agentRepository.save(agent);
        contractRepository.save(contract);
        shipRepository.saveAll(newShipModels);
        return agent;
    }

    /**
     * Syncs DB agent with agent in API
     *
     * @param agent
     * @return
     */
    public Agent syncAgent(Agent agent) throws ApiException {
        ApiClient apiClient = ApiClient.getAgentApiClient(agent.getToken());
        AgentsApi agentsApi = new AgentsApi(apiClient);
        var response = agentsApi.getMyAgent();
        AgentConverter.fromApiAgent(agent, response.getData());
        agentRepository.save(agent);
        return agent;
    }

    public Agent getMainAgent() {
        var currentReset = resetService.getCurrentReset();
        String agentSymbol = System.getenv("AGENT_SYMBOL");
        var agent = agentRepository.findAgentByResetAndSymbol(currentReset, agentSymbol);
        if (agent.isEmpty()) {
            throw new IllegalStateException(
                "Main agent not found for reset: " + currentReset.getResetDate());
        }
        return agent.get();
    }

}
