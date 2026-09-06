package absl.service;

import absl.domain.Practica;
import absl.repository.PracticaRepository;
import absl.service.dto.PracticaDTO;
import absl.service.mapper.PracticaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.Practica}.
 */
@Service
@Transactional
public class PracticaService {

    private static final Logger LOG = LoggerFactory.getLogger(PracticaService.class);

    private final PracticaRepository practicaRepository;

    private final PracticaMapper practicaMapper;

    public PracticaService(PracticaRepository practicaRepository, PracticaMapper practicaMapper) {
        this.practicaRepository = practicaRepository;
        this.practicaMapper = practicaMapper;
    }

    /**
     * Save a practica.
     *
     * @param practicaDTO the entity to save.
     * @return the persisted entity.
     */
    public PracticaDTO save(PracticaDTO practicaDTO) {
        LOG.debug("Request to save Practica : {}", practicaDTO);
        Practica practica = practicaMapper.toEntity(practicaDTO);
        practica = practicaRepository.save(practica);
        return practicaMapper.toDto(practica);
    }

    /**
     * Update a practica.
     *
     * @param practicaDTO the entity to save.
     * @return the persisted entity.
     */
    public PracticaDTO update(PracticaDTO practicaDTO) {
        LOG.debug("Request to update Practica : {}", practicaDTO);
        Practica practica = practicaMapper.toEntity(practicaDTO);
        practica = practicaRepository.save(practica);
        return practicaMapper.toDto(practica);
    }

    /**
     * Partially update a practica.
     *
     * @param practicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PracticaDTO> partialUpdate(PracticaDTO practicaDTO) {
        LOG.debug("Request to partially update Practica : {}", practicaDTO);

        return practicaRepository
            .findById(practicaDTO.getId())
            .map(existingPractica -> {
                practicaMapper.partialUpdate(existingPractica, practicaDTO);

                return existingPractica;
            })
            .map(practicaRepository::save)
            .map(practicaMapper::toDto);
    }

    /**
     * Get all the practicas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PracticaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return practicaRepository.findAllWithEagerRelationships(pageable).map(practicaMapper::toDto);
    }

    /**
     * Get one practica by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PracticaDTO> findOne(Long id) {
        LOG.debug("Request to get Practica : {}", id);
        return practicaRepository.findOneWithEagerRelationships(id).map(practicaMapper::toDto);
    }

    /**
     * Delete the practica by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Practica : {}", id);
        practicaRepository.deleteById(id);
    }
}
