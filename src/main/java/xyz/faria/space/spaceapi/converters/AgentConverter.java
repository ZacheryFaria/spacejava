package xyz.faria.space.spaceapi.converters;

import xyz.faria.space.models.Agent;

public class AgentConverter {
    public static Agent fromApiAgent(Agent ag, xyz.faria.space.spaceapi.model.Agent apiAgent) {
        ag.setAccountId(apiAgent.getAccountId());
        ag.setSymbol(apiAgent.getSymbol());
        ag.setCredits(apiAgent.getCredits());
        ag.setHeadquarters(apiAgent.getHeadquarters());
        ag.setStartingFaction(apiAgent.getStartingFaction());
        return ag;
    }

    public static Agent fromApiAgent(xyz.faria.space.spaceapi.model.Agent apiAgent) {
        return fromApiAgent(new Agent(), apiAgent);
    }
}
