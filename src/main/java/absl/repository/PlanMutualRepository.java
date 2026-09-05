package absl.repository;

import absl.domain.PlanMutual;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlanMutual entity.
 */
@Repository
public interface PlanMutualRepository extends JpaRepository<PlanMutual, Long> {
    default Optional<PlanMutual> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PlanMutual> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PlanMutual> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select planMutual from PlanMutual planMutual left join fetch planMutual.mutual",
        countQuery = "select count(planMutual) from PlanMutual planMutual"
    )
    Page<PlanMutual> findAllWithToOneRelationships(Pageable pageable);

    @Query("select planMutual from PlanMutual planMutual left join fetch planMutual.mutual")
    List<PlanMutual> findAllWithToOneRelationships();

    @Query("select planMutual from PlanMutual planMutual left join fetch planMutual.mutual where planMutual.id =:id")
    Optional<PlanMutual> findOneWithToOneRelationships(@Param("id") Long id);
}
