package absl.service;

import absl.domain.PlanMutual;
import absl.repository.PlanMutualRepository;
import absl.service.dto.PlanMutualDTO;
import absl.service.mapper.PlanMutualMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.PlanMutual}.
 */
@Service
@Transactional
public class PlanMutualService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanMutualService.class);

    private final PlanMutualRepository planMutualRepository;

    private final PlanMutualMapper planMutualMapper;

    public PlanMutualService(PlanMutualRepository planMutualRepository, PlanMutualMapper planMutualMapper) {
        this.planMutualRepository = planMutualRepository;
        this.planMutualMapper = planMutualMapper;
    }

    /**
     * Save a planMutual.
     *
     * @param planMutualDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanMutualDTO save(PlanMutualDTO planMutualDTO) {
        LOG.debug("Request to save PlanMutual : {}", planMutualDTO);
        PlanMutual planMutual = planMutualMapper.toEntity(planMutualDTO);
        planMutual = planMutualRepository.save(planMutual);
        return planMutualMapper.toDto(planMutual);
    }

    /**
     * Update a planMutual.
     *
     * @param planMutualDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanMutualDTO update(PlanMutualDTO planMutualDTO) {
        LOG.debug("Request to update PlanMutual : {}", planMutualDTO);
        PlanMutual planMutual = planMutualMapper.toEntity(planMutualDTO);
        planMutual = planMutualRepository.save(planMutual);
        return planMutualMapper.toDto(planMutual);
    }

    /**
     * Partially update a planMutual.
     *
     * @param planMutualDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlanMutualDTO> partialUpdate(PlanMutualDTO planMutualDTO) {
        LOG.debug("Request to partially update PlanMutual : {}", planMutualDTO);

        return planMutualRepository
            .findById(planMutualDTO.getId())
            .map(existingPlanMutual -> {
                planMutualMapper.partialUpdate(existingPlanMutual, planMutualDTO);

                return existingPlanMutual;
            })
            .map(planMutualRepository::save)
            .map(planMutualMapper::toDto);
    }

    /**
     * Get all the planMutuals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanMutualDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlanMutuals");
        return planMutualRepository.findAll(pageable).map(planMutualMapper::toDto);
    }

    /**
     * Get all the planMutuals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PlanMutualDTO> findAllWithEagerRelationships(Pageable pageable) {
        return planMutualRepository.findAllWithEagerRelationships(pageable).map(planMutualMapper::toDto);
    }

    /**
     * Get one planMutual by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanMutualDTO> findOne(Long id) {
        LOG.debug("Request to get PlanMutual : {}", id);
        return planMutualRepository.findOneWithEagerRelationships(id).map(planMutualMapper::toDto);
    }

    /**
     * Delete the planMutual by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PlanMutual : {}", id);
        planMutualRepository.deleteById(id);
    }
}
