package absl.repository;

import absl.domain.Paquete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Paquete entity.
 */
@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    default Optional<Paquete> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Paquete> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Paquete> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select paquete from Paquete paquete left join fetch paquete.plan",
        countQuery = "select count(paquete) from Paquete paquete"
    )
    Page<Paquete> findAllWithToOneRelationships(Pageable pageable);

    @Query("select paquete from Paquete paquete left join fetch paquete.plan")
    List<Paquete> findAllWithToOneRelationships();

    @Query("select paquete from Paquete paquete left join fetch paquete.plan where paquete.id =:id")
    Optional<Paquete> findOneWithToOneRelationships(@Param("id") Long id);
}
