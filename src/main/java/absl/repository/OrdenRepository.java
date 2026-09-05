package absl.repository;

import absl.domain.Orden;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    default Optional<Orden> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Orden> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Orden> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orden from Orden orden left join fetch orden.paquete left join fetch orden.bioquimico",
        countQuery = "select count(orden) from Orden orden"
    )
    Page<Orden> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orden from Orden orden left join fetch orden.paquete left join fetch orden.bioquimico")
    List<Orden> findAllWithToOneRelationships();

    @Query("select orden from Orden orden left join fetch orden.paquete left join fetch orden.bioquimico where orden.id =:id")
    Optional<Orden> findOneWithToOneRelationships(@Param("id") Long id);
}
