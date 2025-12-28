package xyz.faria.space.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Agent;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends CrudRepository<Agent, UUID> {
    Optional<Agent> findByResetDate(Date resetDate);
}
