package absl.repository;

import absl.domain.Practica;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Practica entity.
 */
@Repository
public interface PracticaRepository extends JpaRepository<Practica, Long>, JpaSpecificationExecutor<Practica> {
    default Optional<Practica> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Practica> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Practica> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select practica from Practica practica left join fetch practica.prestacion",
        countQuery = "select count(practica) from Practica practica"
    )
    Page<Practica> findAllWithToOneRelationships(Pageable pageable);

    @Query("select practica from Practica practica left join fetch practica.prestacion")
    List<Practica> findAllWithToOneRelationships();

    @Query("select practica from Practica practica left join fetch practica.prestacion where practica.id =:id")
    Optional<Practica> findOneWithToOneRelationships(@Param("id") Long id);
}
