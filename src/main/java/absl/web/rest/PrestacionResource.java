package absl.web.rest;

import absl.repository.PrestacionRepository;
import absl.service.PrestacionService;
import absl.service.dto.PrestacionDTO;
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
 * REST controller for managing {@link absl.domain.Prestacion}.
 */
@RestController
@RequestMapping("/api/prestacions")
public class PrestacionResource {

    private static final Logger LOG = LoggerFactory.getLogger(PrestacionResource.class);

    private static final String ENTITY_NAME = "prestacion";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final PrestacionService prestacionService;

    private final PrestacionRepository prestacionRepository;

    public PrestacionResource(PrestacionService prestacionService, PrestacionRepository prestacionRepository) {
        this.prestacionService = prestacionService;
        this.prestacionRepository = prestacionRepository;
    }

    /**
     * {@code POST  /prestacions} : Create a new prestacion.
     *
     * @param prestacionDTO the prestacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prestacionDTO, or with status {@code 400 (Bad Request)} if the prestacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PrestacionDTO> createPrestacion(@Valid @RequestBody PrestacionDTO prestacionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Prestacion : {}", prestacionDTO);
        if (prestacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new prestacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prestacionDTO = prestacionService.save(prestacionDTO);
        return ResponseEntity.created(new URI("/api/prestacions/" + prestacionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, prestacionDTO.getId().toString()))
            .body(prestacionDTO);
    }

    /**
     * {@code PUT  /prestacions/:id} : Updates an existing prestacion.
     *
     * @param id the id of the prestacionDTO to save.
     * @param prestacionDTO the prestacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestacionDTO,
     * or with status {@code 400 (Bad Request)} if the prestacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prestacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrestacionDTO> updatePrestacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrestacionDTO prestacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Prestacion : {}, {}", id, prestacionDTO);
        if (prestacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prestacionDTO = prestacionService.update(prestacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prestacionDTO.getId().toString()))
            .body(prestacionDTO);
    }

    /**
     * {@code PATCH  /prestacions/:id} : Partial updates given fields of an existing prestacion, field will ignore if it is null
     *
     * @param id the id of the prestacionDTO to save.
     * @param prestacionDTO the prestacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prestacionDTO,
     * or with status {@code 400 (Bad Request)} if the prestacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prestacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prestacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrestacionDTO> partialUpdatePrestacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrestacionDTO prestacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Prestacion partially : {}, {}", id, prestacionDTO);
        if (prestacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prestacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prestacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrestacionDTO> result = prestacionService.partialUpdate(prestacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prestacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prestacions} : get all the Prestacions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Prestacions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PrestacionDTO>> getAllPrestacions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Prestacions");
        Page<PrestacionDTO> page;
        if (eagerload) {
            page = prestacionService.findAllWithEagerRelationships(pageable);
        } else {
            page = prestacionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prestacions/:id} : get the "id" prestacion.
     *
     * @param id the id of the prestacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prestacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrestacionDTO> getPrestacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Prestacion : {}", id);
        Optional<PrestacionDTO> prestacionDTO = prestacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prestacionDTO);
    }

    /**
     * {@code DELETE  /prestacions/:id} : delete the "id" prestacion.
     *
     * @param id the id of the prestacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Prestacion : {}", id);
        prestacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
