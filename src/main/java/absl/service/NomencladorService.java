package absl.service;

import absl.domain.Nomenclador;
import absl.repository.NomencladorRepository;
import absl.service.dto.NomencladorDTO;
import absl.service.mapper.NomencladorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.Nomenclador}.
 */
@Service
@Transactional
public class NomencladorService {

    private static final Logger LOG = LoggerFactory.getLogger(NomencladorService.class);

    private final NomencladorRepository nomencladorRepository;

    private final NomencladorMapper nomencladorMapper;

    public NomencladorService(NomencladorRepository nomencladorRepository, NomencladorMapper nomencladorMapper) {
        this.nomencladorRepository = nomencladorRepository;
        this.nomencladorMapper = nomencladorMapper;
    }

    /**
     * Save a nomenclador.
     *
     * @param nomencladorDTO the entity to save.
     * @return the persisted entity.
     */
    public NomencladorDTO save(NomencladorDTO nomencladorDTO) {
        LOG.debug("Request to save Nomenclador : {}", nomencladorDTO);
        Nomenclador nomenclador = nomencladorMapper.toEntity(nomencladorDTO);
        nomenclador = nomencladorRepository.save(nomenclador);
        return nomencladorMapper.toDto(nomenclador);
    }

    /**
     * Update a nomenclador.
     *
     * @param nomencladorDTO the entity to save.
     * @return the persisted entity.
     */
    public NomencladorDTO update(NomencladorDTO nomencladorDTO) {
        LOG.debug("Request to update Nomenclador : {}", nomencladorDTO);
        Nomenclador nomenclador = nomencladorMapper.toEntity(nomencladorDTO);
        nomenclador = nomencladorRepository.save(nomenclador);
        return nomencladorMapper.toDto(nomenclador);
    }

    /**
     * Partially update a nomenclador.
     *
     * @param nomencladorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NomencladorDTO> partialUpdate(NomencladorDTO nomencladorDTO) {
        LOG.debug("Request to partially update Nomenclador : {}", nomencladorDTO);

        return nomencladorRepository
            .findById(nomencladorDTO.getId())
            .map(existingNomenclador -> {
                nomencladorMapper.partialUpdate(existingNomenclador, nomencladorDTO);

                return existingNomenclador;
            })
            .map(nomencladorRepository::save)
            .map(nomencladorMapper::toDto);
    }

    /**
     * Get all the nomencladors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NomencladorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Nomencladors");
        return nomencladorRepository.findAll(pageable).map(nomencladorMapper::toDto);
    }

    /**
     * Get all the nomencladors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NomencladorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nomencladorRepository.findAllWithEagerRelationships(pageable).map(nomencladorMapper::toDto);
    }

    /**
     * Get one nomenclador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NomencladorDTO> findOne(Long id) {
        LOG.debug("Request to get Nomenclador : {}", id);
        return nomencladorRepository.findOneWithEagerRelationships(id).map(nomencladorMapper::toDto);
    }

    /**
     * Delete the nomenclador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Nomenclador : {}", id);
        nomencladorRepository.deleteById(id);
    }
}
