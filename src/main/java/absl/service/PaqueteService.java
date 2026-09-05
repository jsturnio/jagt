package absl.service;

import absl.domain.Paquete;
import absl.repository.PaqueteRepository;
import absl.service.dto.PaqueteDTO;
import absl.service.mapper.PaqueteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.Paquete}.
 */
@Service
@Transactional
public class PaqueteService {

    private static final Logger LOG = LoggerFactory.getLogger(PaqueteService.class);

    private final PaqueteRepository paqueteRepository;

    private final PaqueteMapper paqueteMapper;

    public PaqueteService(PaqueteRepository paqueteRepository, PaqueteMapper paqueteMapper) {
        this.paqueteRepository = paqueteRepository;
        this.paqueteMapper = paqueteMapper;
    }

    /**
     * Save a paquete.
     *
     * @param paqueteDTO the entity to save.
     * @return the persisted entity.
     */
    public PaqueteDTO save(PaqueteDTO paqueteDTO) {
        LOG.debug("Request to save Paquete : {}", paqueteDTO);
        Paquete paquete = paqueteMapper.toEntity(paqueteDTO);
        paquete = paqueteRepository.save(paquete);
        return paqueteMapper.toDto(paquete);
    }

    /**
     * Update a paquete.
     *
     * @param paqueteDTO the entity to save.
     * @return the persisted entity.
     */
    public PaqueteDTO update(PaqueteDTO paqueteDTO) {
        LOG.debug("Request to update Paquete : {}", paqueteDTO);
        Paquete paquete = paqueteMapper.toEntity(paqueteDTO);
        paquete = paqueteRepository.save(paquete);
        return paqueteMapper.toDto(paquete);
    }

    /**
     * Partially update a paquete.
     *
     * @param paqueteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaqueteDTO> partialUpdate(PaqueteDTO paqueteDTO) {
        LOG.debug("Request to partially update Paquete : {}", paqueteDTO);

        return paqueteRepository
            .findById(paqueteDTO.getId())
            .map(existingPaquete -> {
                paqueteMapper.partialUpdate(existingPaquete, paqueteDTO);

                return existingPaquete;
            })
            .map(paqueteRepository::save)
            .map(paqueteMapper::toDto);
    }

    /**
     * Get all the paquetes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaqueteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Paquetes");
        return paqueteRepository.findAll(pageable).map(paqueteMapper::toDto);
    }

    /**
     * Get all the paquetes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PaqueteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paqueteRepository.findAllWithEagerRelationships(pageable).map(paqueteMapper::toDto);
    }

    /**
     * Get one paquete by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaqueteDTO> findOne(Long id) {
        LOG.debug("Request to get Paquete : {}", id);
        return paqueteRepository.findOneWithEagerRelationships(id).map(paqueteMapper::toDto);
    }

    /**
     * Delete the paquete by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Paquete : {}", id);
        paqueteRepository.deleteById(id);
    }
}
