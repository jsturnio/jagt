package absl.service;

import absl.domain.Prestacion;
import absl.repository.PrestacionRepository;
import absl.service.dto.PrestacionDTO;
import absl.service.mapper.PrestacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.Prestacion}.
 */
@Service
@Transactional
public class PrestacionService {

    private static final Logger LOG = LoggerFactory.getLogger(PrestacionService.class);

    private final PrestacionRepository prestacionRepository;

    private final PrestacionMapper prestacionMapper;

    public PrestacionService(PrestacionRepository prestacionRepository, PrestacionMapper prestacionMapper) {
        this.prestacionRepository = prestacionRepository;
        this.prestacionMapper = prestacionMapper;
    }

    /**
     * Save a prestacion.
     *
     * @param prestacionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestacionDTO save(PrestacionDTO prestacionDTO) {
        LOG.debug("Request to save Prestacion : {}", prestacionDTO);
        Prestacion prestacion = prestacionMapper.toEntity(prestacionDTO);
        prestacion = prestacionRepository.save(prestacion);
        return prestacionMapper.toDto(prestacion);
    }

    /**
     * Update a prestacion.
     *
     * @param prestacionDTO the entity to save.
     * @return the persisted entity.
     */
    public PrestacionDTO update(PrestacionDTO prestacionDTO) {
        LOG.debug("Request to update Prestacion : {}", prestacionDTO);
        Prestacion prestacion = prestacionMapper.toEntity(prestacionDTO);
        prestacion = prestacionRepository.save(prestacion);
        return prestacionMapper.toDto(prestacion);
    }

    /**
     * Partially update a prestacion.
     *
     * @param prestacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PrestacionDTO> partialUpdate(PrestacionDTO prestacionDTO) {
        LOG.debug("Request to partially update Prestacion : {}", prestacionDTO);

        return prestacionRepository
            .findById(prestacionDTO.getId())
            .map(existingPrestacion -> {
                prestacionMapper.partialUpdate(existingPrestacion, prestacionDTO);

                return existingPrestacion;
            })
            .map(prestacionRepository::save)
            .map(prestacionMapper::toDto);
    }

    /**
     * Get all the prestacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PrestacionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Prestacions");
        return prestacionRepository.findAll(pageable).map(prestacionMapper::toDto);
    }

    /**
     * Get all the prestacions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PrestacionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prestacionRepository.findAllWithEagerRelationships(pageable).map(prestacionMapper::toDto);
    }

    /**
     * Get one prestacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrestacionDTO> findOne(Long id) {
        LOG.debug("Request to get Prestacion : {}", id);
        return prestacionRepository.findOneWithEagerRelationships(id).map(prestacionMapper::toDto);
    }

    /**
     * Delete the prestacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Prestacion : {}", id);
        prestacionRepository.deleteById(id);
    }
}
