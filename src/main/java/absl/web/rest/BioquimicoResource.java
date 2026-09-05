package absl.web.rest;

import absl.repository.BioquimicoRepository;
import absl.service.BioquimicoService;
import absl.service.dto.BioquimicoDTO;
import absl.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link absl.domain.Bioquimico}.
 */
@RestController
@RequestMapping("/api/bioquimicos")
public class BioquimicoResource {

    private static final Logger LOG = LoggerFactory.getLogger(BioquimicoResource.class);

    private static final String ENTITY_NAME = "bioquimico";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final BioquimicoService bioquimicoService;

    private final BioquimicoRepository bioquimicoRepository;

    public BioquimicoResource(BioquimicoService bioquimicoService, BioquimicoRepository bioquimicoRepository) {
        this.bioquimicoService = bioquimicoService;
        this.bioquimicoRepository = bioquimicoRepository;
    }

    /**
     * {@code POST  /bioquimicos} : Create a new bioquimico.
     *
     * @param bioquimicoDTO the bioquimicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bioquimicoDTO, or with status {@code 400 (Bad Request)} if the bioquimico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BioquimicoDTO> createBioquimico(@Valid @RequestBody BioquimicoDTO bioquimicoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Bioquimico : {}", bioquimicoDTO);
        if (bioquimicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new bioquimico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bioquimicoDTO = bioquimicoService.save(bioquimicoDTO);
        return ResponseEntity.created(new URI("/api/bioquimicos/" + bioquimicoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, bioquimicoDTO.getId().toString()))
            .body(bioquimicoDTO);
    }

    /**
     * {@code PUT  /bioquimicos/:id} : Updates an existing bioquimico.
     *
     * @param id the id of the bioquimicoDTO to save.
     * @param bioquimicoDTO the bioquimicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bioquimicoDTO,
     * or with status {@code 400 (Bad Request)} if the bioquimicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bioquimicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BioquimicoDTO> updateBioquimico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BioquimicoDTO bioquimicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bioquimico : {}, {}", id, bioquimicoDTO);
        if (bioquimicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bioquimicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bioquimicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bioquimicoDTO = bioquimicoService.update(bioquimicoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bioquimicoDTO.getId().toString()))
            .body(bioquimicoDTO);
    }

    /**
     * {@code PATCH  /bioquimicos/:id} : Partial updates given fields of an existing bioquimico, field will ignore if it is null
     *
     * @param id the id of the bioquimicoDTO to save.
     * @param bioquimicoDTO the bioquimicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bioquimicoDTO,
     * or with status {@code 400 (Bad Request)} if the bioquimicoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bioquimicoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bioquimicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BioquimicoDTO> partialUpdateBioquimico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BioquimicoDTO bioquimicoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bioquimico partially : {}, {}", id, bioquimicoDTO);
        if (bioquimicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bioquimicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bioquimicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BioquimicoDTO> result = bioquimicoService.partialUpdate(bioquimicoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bioquimicoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bioquimicos} : get all the Bioquimicos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Bioquimicos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BioquimicoDTO>> getAllBioquimicos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Bioquimicos");
        Page<BioquimicoDTO> page;
        if (eagerload) {
            page = bioquimicoService.findAllWithEagerRelationships(pageable);
        } else {
            page = bioquimicoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bioquimicos/:id} : get the "id" bioquimico.
     *
     * @param id the id of the bioquimicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bioquimicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BioquimicoDTO> getBioquimico(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bioquimico : {}", id);
        Optional<BioquimicoDTO> bioquimicoDTO = bioquimicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bioquimicoDTO);
    }

    /**
     * {@code DELETE  /bioquimicos/:id} : delete the "id" bioquimico.
     *
     * @param id the id of the bioquimicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBioquimico(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bioquimico : {}", id);
        bioquimicoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
