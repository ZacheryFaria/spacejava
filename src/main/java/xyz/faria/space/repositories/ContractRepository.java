package xyz.faria.space.repositories;

import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Agent;
import xyz.faria.space.models.Contract;

public interface ContractRepository extends CrudRepository<Contract, String> {

    Contract getContractByAgentAndFulfilled(Agent agent, Boolean fulfilled);
}
