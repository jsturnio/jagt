package absl.repository;

import absl.domain.Prestacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Prestacion entity.
 */
@Repository
public interface PrestacionRepository extends JpaRepository<Prestacion, Long> {
    default Optional<Prestacion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Prestacion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Prestacion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select prestacion from Prestacion prestacion left join fetch prestacion.nomenclador",
        countQuery = "select count(prestacion) from Prestacion prestacion"
    )
    Page<Prestacion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select prestacion from Prestacion prestacion left join fetch prestacion.nomenclador")
    List<Prestacion> findAllWithToOneRelationships();

    @Query("select prestacion from Prestacion prestacion left join fetch prestacion.nomenclador where prestacion.id =:id")
    Optional<Prestacion> findOneWithToOneRelationships(@Param("id") Long id);
}
