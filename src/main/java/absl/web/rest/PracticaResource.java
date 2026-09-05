package absl.web.rest;

import absl.repository.PracticaRepository;
import absl.service.PracticaService;
import absl.service.dto.PracticaDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link absl.domain.Practica}.
 */
@RestController
@RequestMapping("/api/practicas")
public class PracticaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PracticaResource.class);

    private static final String ENTITY_NAME = "practica";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final PracticaService practicaService;

    private final PracticaRepository practicaRepository;

    public PracticaResource(PracticaService practicaService, PracticaRepository practicaRepository) {
        this.practicaService = practicaService;
        this.practicaRepository = practicaRepository;
    }

    /**
     * {@code POST  /practicas} : Create a new practica.
     *
     * @param practicaDTO the practicaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new practicaDTO, or with status {@code 400 (Bad Request)} if the practica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PracticaDTO> createPractica(@Valid @RequestBody PracticaDTO practicaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Practica : {}", practicaDTO);
        if (practicaDTO.getId() != null) {
            throw new BadRequestAlertException("A new practica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        practicaDTO = practicaService.save(practicaDTO);
        return ResponseEntity.created(new URI("/api/practicas/" + practicaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, practicaDTO.getId().toString()))
            .body(practicaDTO);
    }

    /**
     * {@code PUT  /practicas/:id} : Updates an existing practica.
     *
     * @param id the id of the practicaDTO to save.
     * @param practicaDTO the practicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated practicaDTO,
     * or with status {@code 400 (Bad Request)} if the practicaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the practicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PracticaDTO> updatePractica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PracticaDTO practicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Practica : {}, {}", id, practicaDTO);
        if (practicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, practicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!practicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        practicaDTO = practicaService.update(practicaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, practicaDTO.getId().toString()))
            .body(practicaDTO);
    }

    /**
     * {@code PATCH  /practicas/:id} : Partial updates given fields of an existing practica, field will ignore if it is null
     *
     * @param id the id of the practicaDTO to save.
     * @param practicaDTO the practicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated practicaDTO,
     * or with status {@code 400 (Bad Request)} if the practicaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the practicaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the practicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PracticaDTO> partialUpdatePractica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PracticaDTO practicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Practica partially : {}, {}", id, practicaDTO);
        if (practicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, practicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!practicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PracticaDTO> result = practicaService.partialUpdate(practicaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, practicaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /practicas} : get all the Practicas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Practicas in body.
     */
    @GetMapping("")
    public List<PracticaDTO> getAllPracticas(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Practicas");
        if (eagerload) {
            return practicaService.findAllWithEagerRelationships();
        } else {
            return practicaService.findAll();
        }
    }

    /**
     * {@code GET  /practicas/:id} : get the "id" practica.
     *
     * @param id the id of the practicaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the practicaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PracticaDTO> getPractica(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Practica : {}", id);
        Optional<PracticaDTO> practicaDTO = practicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(practicaDTO);
    }

    /**
     * {@code DELETE  /practicas/:id} : delete the "id" practica.
     *
     * @param id the id of the practicaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractica(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Practica : {}", id);
        practicaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
