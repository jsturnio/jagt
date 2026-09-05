package absl.service;

import absl.domain.Mutual;
import absl.repository.MutualRepository;
import absl.service.dto.MutualDTO;
import absl.service.mapper.MutualMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.Mutual}.
 */
@Service
@Transactional
public class MutualService {

    private static final Logger LOG = LoggerFactory.getLogger(MutualService.class);

    private final MutualRepository mutualRepository;

    private final MutualMapper mutualMapper;

    public MutualService(MutualRepository mutualRepository, MutualMapper mutualMapper) {
        this.mutualRepository = mutualRepository;
        this.mutualMapper = mutualMapper;
    }

    /**
     * Save a mutual.
     *
     * @param mutualDTO the entity to save.
     * @return the persisted entity.
     */
    public MutualDTO save(MutualDTO mutualDTO) {
        LOG.debug("Request to save Mutual : {}", mutualDTO);
        Mutual mutual = mutualMapper.toEntity(mutualDTO);
        mutual = mutualRepository.save(mutual);
        return mutualMapper.toDto(mutual);
    }

    /**
     * Update a mutual.
     *
     * @param mutualDTO the entity to save.
     * @return the persisted entity.
     */
    public MutualDTO update(MutualDTO mutualDTO) {
        LOG.debug("Request to update Mutual : {}", mutualDTO);
        Mutual mutual = mutualMapper.toEntity(mutualDTO);
        mutual = mutualRepository.save(mutual);
        return mutualMapper.toDto(mutual);
    }

    /**
     * Partially update a mutual.
     *
     * @param mutualDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MutualDTO> partialUpdate(MutualDTO mutualDTO) {
        LOG.debug("Request to partially update Mutual : {}", mutualDTO);

        return mutualRepository
            .findById(mutualDTO.getId())
            .map(existingMutual -> {
                mutualMapper.partialUpdate(existingMutual, mutualDTO);

                return existingMutual;
            })
            .map(mutualRepository::save)
            .map(mutualMapper::toDto);
    }

    /**
     * Get all the mutuals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MutualDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Mutuals");
        return mutualRepository.findAll(pageable).map(mutualMapper::toDto);
    }

    /**
     * Get one mutual by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MutualDTO> findOne(Long id) {
        LOG.debug("Request to get Mutual : {}", id);
        return mutualRepository.findById(id).map(mutualMapper::toDto);
    }

    /**
     * Delete the mutual by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Mutual : {}", id);
        mutualRepository.deleteById(id);
    }
}
