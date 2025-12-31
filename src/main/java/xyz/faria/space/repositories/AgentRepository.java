package xyz.faria.space.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Agent;

public interface AgentRepository extends CrudRepository<Agent, UUID> {

}
