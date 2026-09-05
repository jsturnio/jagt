package absl.web.rest;

import absl.repository.MutualRepository;
import absl.service.MutualService;
import absl.service.dto.MutualDTO;
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
 * REST controller for managing {@link absl.domain.Mutual}.
 */
@RestController
@RequestMapping("/api/mutuals")
public class MutualResource {

    private static final Logger LOG = LoggerFactory.getLogger(MutualResource.class);

    private static final String ENTITY_NAME = "mutual";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final MutualService mutualService;

    private final MutualRepository mutualRepository;

    public MutualResource(MutualService mutualService, MutualRepository mutualRepository) {
        this.mutualService = mutualService;
        this.mutualRepository = mutualRepository;
    }

    /**
     * {@code POST  /mutuals} : Create a new mutual.
     *
     * @param mutualDTO the mutualDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mutualDTO, or with status {@code 400 (Bad Request)} if the mutual has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MutualDTO> createMutual(@Valid @RequestBody MutualDTO mutualDTO) throws URISyntaxException {
        LOG.debug("REST request to save Mutual : {}", mutualDTO);
        if (mutualDTO.getId() != null) {
            throw new BadRequestAlertException("A new mutual cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mutualDTO = mutualService.save(mutualDTO);
        return ResponseEntity.created(new URI("/api/mutuals/" + mutualDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, mutualDTO.getId().toString()))
            .body(mutualDTO);
    }

    /**
     * {@code PUT  /mutuals/:id} : Updates an existing mutual.
     *
     * @param id the id of the mutualDTO to save.
     * @param mutualDTO the mutualDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mutualDTO,
     * or with status {@code 400 (Bad Request)} if the mutualDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mutualDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MutualDTO> updateMutual(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MutualDTO mutualDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mutual : {}, {}", id, mutualDTO);
        if (mutualDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mutualDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mutualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mutualDTO = mutualService.update(mutualDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mutualDTO.getId().toString()))
            .body(mutualDTO);
    }

    /**
     * {@code PATCH  /mutuals/:id} : Partial updates given fields of an existing mutual, field will ignore if it is null
     *
     * @param id the id of the mutualDTO to save.
     * @param mutualDTO the mutualDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mutualDTO,
     * or with status {@code 400 (Bad Request)} if the mutualDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mutualDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mutualDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MutualDTO> partialUpdateMutual(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MutualDTO mutualDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mutual partially : {}, {}", id, mutualDTO);
        if (mutualDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mutualDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mutualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MutualDTO> result = mutualService.partialUpdate(mutualDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mutualDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mutuals} : get all the Mutuals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Mutuals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MutualDTO>> getAllMutuals(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Mutuals");
        Page<MutualDTO> page = mutualService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mutuals/:id} : get the "id" mutual.
     *
     * @param id the id of the mutualDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mutualDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MutualDTO> getMutual(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Mutual : {}", id);
        Optional<MutualDTO> mutualDTO = mutualService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mutualDTO);
    }

    /**
     * {@code DELETE  /mutuals/:id} : delete the "id" mutual.
     *
     * @param id the id of the mutualDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMutual(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Mutual : {}", id);
        mutualService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
