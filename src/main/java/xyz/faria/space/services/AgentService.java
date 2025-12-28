package xyz.faria.space.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.faria.space.models.Agent;
import xyz.faria.space.repositories.AgentRepository;
import xyz.faria.space.spaceapi.Utils;
import xyz.faria.space.spaceapi.api.AgentsApi;
import xyz.faria.space.spaceapi.api.GlobalApi;
import xyz.faria.space.spaceapi.client.ApiClient;
import xyz.faria.space.spaceapi.client.ApiException;
import xyz.faria.space.spaceapi.converters.AgentConverter;
import xyz.faria.space.spaceapi.model.FactionSymbol;
import xyz.faria.space.spaceapi.model.RegisterRequest;

import java.sql.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;

    public @Nullable Agent getAgentByResetDate(Date resetDate) {
        return agentRepository.findByResetDate(resetDate).orElse(null);
    }

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
        agent.setResetDate(Utils.getCurrentResetDate());
        agentRepository.save(agent);
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
}
