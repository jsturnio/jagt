package absl.web.rest;

import absl.repository.PaqueteRepository;
import absl.service.PaqueteService;
import absl.service.dto.PaqueteDTO;
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
 * REST controller for managing {@link absl.domain.Paquete}.
 */
@RestController
@RequestMapping("/api/paquetes")
public class PaqueteResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaqueteResource.class);

    private static final String ENTITY_NAME = "paquete";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final PaqueteService paqueteService;

    private final PaqueteRepository paqueteRepository;

    public PaqueteResource(PaqueteService paqueteService, PaqueteRepository paqueteRepository) {
        this.paqueteService = paqueteService;
        this.paqueteRepository = paqueteRepository;
    }

    /**
     * {@code POST  /paquetes} : Create a new paquete.
     *
     * @param paqueteDTO the paqueteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paqueteDTO, or with status {@code 400 (Bad Request)} if the paquete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaqueteDTO> createPaquete(@Valid @RequestBody PaqueteDTO paqueteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Paquete : {}", paqueteDTO);
        if (paqueteDTO.getId() != null) {
            throw new BadRequestAlertException("A new paquete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paqueteDTO = paqueteService.save(paqueteDTO);
        return ResponseEntity.created(new URI("/api/paquetes/" + paqueteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, paqueteDTO.getId().toString()))
            .body(paqueteDTO);
    }

    /**
     * {@code PUT  /paquetes/:id} : Updates an existing paquete.
     *
     * @param id the id of the paqueteDTO to save.
     * @param paqueteDTO the paqueteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paqueteDTO,
     * or with status {@code 400 (Bad Request)} if the paqueteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paqueteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaqueteDTO> updatePaquete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaqueteDTO paqueteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Paquete : {}, {}", id, paqueteDTO);
        if (paqueteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paqueteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paqueteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paqueteDTO = paqueteService.update(paqueteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paqueteDTO.getId().toString()))
            .body(paqueteDTO);
    }

    /**
     * {@code PATCH  /paquetes/:id} : Partial updates given fields of an existing paquete, field will ignore if it is null
     *
     * @param id the id of the paqueteDTO to save.
     * @param paqueteDTO the paqueteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paqueteDTO,
     * or with status {@code 400 (Bad Request)} if the paqueteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paqueteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paqueteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaqueteDTO> partialUpdatePaquete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaqueteDTO paqueteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Paquete partially : {}, {}", id, paqueteDTO);
        if (paqueteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paqueteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paqueteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaqueteDTO> result = paqueteService.partialUpdate(paqueteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paqueteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paquetes} : get all the Paquetes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Paquetes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaqueteDTO>> getAllPaquetes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Paquetes");
        Page<PaqueteDTO> page;
        if (eagerload) {
            page = paqueteService.findAllWithEagerRelationships(pageable);
        } else {
            page = paqueteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paquetes/:id} : get the "id" paquete.
     *
     * @param id the id of the paqueteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paqueteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaqueteDTO> getPaquete(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Paquete : {}", id);
        Optional<PaqueteDTO> paqueteDTO = paqueteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paqueteDTO);
    }

    /**
     * {@code DELETE  /paquetes/:id} : delete the "id" paquete.
     *
     * @param id the id of the paqueteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaquete(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Paquete : {}", id);
        paqueteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
