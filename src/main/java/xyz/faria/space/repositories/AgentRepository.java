package xyz.faria.space.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Reset;

public interface AgentRepository extends CrudRepository<Agent, UUID> {

    Optional<Agent> findAgentByReset(Reset reset);
}
