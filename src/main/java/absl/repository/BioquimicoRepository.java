package absl.repository;

import absl.domain.Bioquimico;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bioquimico entity.
 */
@Repository
public interface BioquimicoRepository extends JpaRepository<Bioquimico, Long> {
    default Optional<Bioquimico> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Bioquimico> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Bioquimico> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bioquimico from Bioquimico bioquimico left join fetch bioquimico.user",
        countQuery = "select count(bioquimico) from Bioquimico bioquimico"
    )
    Page<Bioquimico> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bioquimico from Bioquimico bioquimico left join fetch bioquimico.user")
    List<Bioquimico> findAllWithToOneRelationships();

    @Query("select bioquimico from Bioquimico bioquimico left join fetch bioquimico.user where bioquimico.id =:id")
    Optional<Bioquimico> findOneWithToOneRelationships(@Param("id") Long id);
}
