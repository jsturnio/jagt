package absl.repository;

import absl.domain.Nomenclador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Nomenclador entity.
 */
@Repository
public interface NomencladorRepository extends JpaRepository<Nomenclador, Long> {
    default Optional<Nomenclador> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Nomenclador> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Nomenclador> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select nomenclador from Nomenclador nomenclador left join fetch nomenclador.mutual",
        countQuery = "select count(nomenclador) from Nomenclador nomenclador"
    )
    Page<Nomenclador> findAllWithToOneRelationships(Pageable pageable);

    @Query("select nomenclador from Nomenclador nomenclador left join fetch nomenclador.mutual")
    List<Nomenclador> findAllWithToOneRelationships();

    @Query("select nomenclador from Nomenclador nomenclador left join fetch nomenclador.mutual where nomenclador.id =:id")
    Optional<Nomenclador> findOneWithToOneRelationships(@Param("id") Long id);
}
