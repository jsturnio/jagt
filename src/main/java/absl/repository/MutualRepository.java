package absl.repository;

import absl.domain.Mutual;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Mutual entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MutualRepository extends JpaRepository<Mutual, Long> {}
