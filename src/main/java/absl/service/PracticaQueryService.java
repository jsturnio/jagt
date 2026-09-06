package absl.service;

import absl.domain.*; // for static metamodels
import absl.domain.Practica;
import absl.repository.PracticaRepository;
import absl.service.criteria.PracticaCriteria;
import absl.service.dto.PracticaDTO;
import absl.service.mapper.PracticaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Practica} entities in the database.
 * The main input is a {@link PracticaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PracticaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PracticaQueryService extends QueryService<Practica> {

    private static final Logger LOG = LoggerFactory.getLogger(PracticaQueryService.class);

    private final PracticaRepository practicaRepository;

    private final PracticaMapper practicaMapper;

    public PracticaQueryService(PracticaRepository practicaRepository, PracticaMapper practicaMapper) {
        this.practicaRepository = practicaRepository;
        this.practicaMapper = practicaMapper;
    }

    /**
     * Return a {@link Page} of {@link PracticaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PracticaDTO> findByCriteria(PracticaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Practica> specification = createSpecification(criteria);
        return practicaRepository.findAll(specification, page).map(practicaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PracticaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Practica> specification = createSpecification(criteria);
        return practicaRepository.count(specification);
    }

    /**
     * Function to convert {@link PracticaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Practica> createSpecification(PracticaCriteria criteria) {
        Specification<Practica> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Practica_.prestacion, JoinType.LEFT);
                root.fetch(Practica_.orden, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Practica_.id),
                    buildRangeSpecification(criteria.getCantidad(), Practica_.cantidad),
                    buildRangeSpecification(criteria.getPvalor(), Practica_.pvalor),
                    buildSpecification(criteria.getPrestacionId(), root ->
                        root.join(Practica_.prestacion, JoinType.LEFT).get(Prestacion_.id)
                    ),
                    buildSpecification(criteria.getOrdenId(), root -> root.join(Practica_.orden, JoinType.LEFT).get(Orden_.id))
                )
            );
        }
        return specification;
    }
}
