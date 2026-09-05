package absl.service;

import absl.domain.Bioquimico;
import absl.repository.BioquimicoRepository;
import absl.service.dto.BioquimicoDTO;
import absl.service.mapper.BioquimicoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link absl.domain.Bioquimico}.
 */
@Service
@Transactional
public class BioquimicoService {

    private static final Logger LOG = LoggerFactory.getLogger(BioquimicoService.class);

    private final BioquimicoRepository bioquimicoRepository;

    private final BioquimicoMapper bioquimicoMapper;

    public BioquimicoService(BioquimicoRepository bioquimicoRepository, BioquimicoMapper bioquimicoMapper) {
        this.bioquimicoRepository = bioquimicoRepository;
        this.bioquimicoMapper = bioquimicoMapper;
    }

    /**
     * Save a bioquimico.
     *
     * @param bioquimicoDTO the entity to save.
     * @return the persisted entity.
     */
    public BioquimicoDTO save(BioquimicoDTO bioquimicoDTO) {
        LOG.debug("Request to save Bioquimico : {}", bioquimicoDTO);
        Bioquimico bioquimico = bioquimicoMapper.toEntity(bioquimicoDTO);
        bioquimico = bioquimicoRepository.save(bioquimico);
        return bioquimicoMapper.toDto(bioquimico);
    }

    /**
     * Update a bioquimico.
     *
     * @param bioquimicoDTO the entity to save.
     * @return the persisted entity.
     */
    public BioquimicoDTO update(BioquimicoDTO bioquimicoDTO) {
        LOG.debug("Request to update Bioquimico : {}", bioquimicoDTO);
        Bioquimico bioquimico = bioquimicoMapper.toEntity(bioquimicoDTO);
        bioquimico = bioquimicoRepository.save(bioquimico);
        return bioquimicoMapper.toDto(bioquimico);
    }

    /**
     * Partially update a bioquimico.
     *
     * @param bioquimicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BioquimicoDTO> partialUpdate(BioquimicoDTO bioquimicoDTO) {
        LOG.debug("Request to partially update Bioquimico : {}", bioquimicoDTO);

        return bioquimicoRepository
            .findById(bioquimicoDTO.getId())
            .map(existingBioquimico -> {
                bioquimicoMapper.partialUpdate(existingBioquimico, bioquimicoDTO);

                return existingBioquimico;
            })
            .map(bioquimicoRepository::save)
            .map(bioquimicoMapper::toDto);
    }

    /**
     * Get all the bioquimicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BioquimicoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Bioquimicos");
        return bioquimicoRepository.findAll(pageable).map(bioquimicoMapper::toDto);
    }

    /**
     * Get all the bioquimicos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BioquimicoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bioquimicoRepository.findAllWithEagerRelationships(pageable).map(bioquimicoMapper::toDto);
    }

    /**
     * Get one bioquimico by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BioquimicoDTO> findOne(Long id) {
        LOG.debug("Request to get Bioquimico : {}", id);
        return bioquimicoRepository.findOneWithEagerRelationships(id).map(bioquimicoMapper::toDto);
    }

    /**
     * Delete the bioquimico by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Bioquimico : {}", id);
        bioquimicoRepository.deleteById(id);
    }
}
