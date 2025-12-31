package xyz.faria.space.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.System;


public interface SystemRepository extends CrudRepository<System, Long> {

    Optional<System> findBySymbol(String symbol);

}
