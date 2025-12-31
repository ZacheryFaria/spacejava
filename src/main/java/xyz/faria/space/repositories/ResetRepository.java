package xyz.faria.space.repositories;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import xyz.faria.space.models.Reset;

public interface ResetRepository extends CrudRepository<Reset, Long> {

    Optional<Reset> findByResetDate(OffsetDateTime resetDate);

}
