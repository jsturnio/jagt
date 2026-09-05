package absl.web.rest;

import absl.repository.NomencladorRepository;
import absl.service.NomencladorService;
import absl.service.dto.NomencladorDTO;
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
 * REST controller for managing {@link absl.domain.Nomenclador}.
 */
@RestController
@RequestMapping("/api/nomencladors")
public class NomencladorResource {

    private static final Logger LOG = LoggerFactory.getLogger(NomencladorResource.class);

    private static final String ENTITY_NAME = "nomenclador";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final NomencladorService nomencladorService;

    private final NomencladorRepository nomencladorRepository;

    public NomencladorResource(NomencladorService nomencladorService, NomencladorRepository nomencladorRepository) {
        this.nomencladorService = nomencladorService;
        this.nomencladorRepository = nomencladorRepository;
    }

    /**
     * {@code POST  /nomencladors} : Create a new nomenclador.
     *
     * @param nomencladorDTO the nomencladorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nomencladorDTO, or with status {@code 400 (Bad Request)} if the nomenclador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NomencladorDTO> createNomenclador(@Valid @RequestBody NomencladorDTO nomencladorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Nomenclador : {}", nomencladorDTO);
        if (nomencladorDTO.getId() != null) {
            throw new BadRequestAlertException("A new nomenclador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nomencladorDTO = nomencladorService.save(nomencladorDTO);
        return ResponseEntity.created(new URI("/api/nomencladors/" + nomencladorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, nomencladorDTO.getId().toString()))
            .body(nomencladorDTO);
    }

    /**
     * {@code PUT  /nomencladors/:id} : Updates an existing nomenclador.
     *
     * @param id the id of the nomencladorDTO to save.
     * @param nomencladorDTO the nomencladorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nomencladorDTO,
     * or with status {@code 400 (Bad Request)} if the nomencladorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nomencladorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NomencladorDTO> updateNomenclador(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NomencladorDTO nomencladorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Nomenclador : {}, {}", id, nomencladorDTO);
        if (nomencladorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nomencladorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nomencladorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nomencladorDTO = nomencladorService.update(nomencladorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nomencladorDTO.getId().toString()))
            .body(nomencladorDTO);
    }

    /**
     * {@code PATCH  /nomencladors/:id} : Partial updates given fields of an existing nomenclador, field will ignore if it is null
     *
     * @param id the id of the nomencladorDTO to save.
     * @param nomencladorDTO the nomencladorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nomencladorDTO,
     * or with status {@code 400 (Bad Request)} if the nomencladorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nomencladorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nomencladorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NomencladorDTO> partialUpdateNomenclador(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NomencladorDTO nomencladorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Nomenclador partially : {}, {}", id, nomencladorDTO);
        if (nomencladorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nomencladorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nomencladorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NomencladorDTO> result = nomencladorService.partialUpdate(nomencladorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nomencladorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nomencladors} : get all the Nomencladors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Nomencladors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NomencladorDTO>> getAllNomencladors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Nomencladors");
        Page<NomencladorDTO> page;
        if (eagerload) {
            page = nomencladorService.findAllWithEagerRelationships(pageable);
        } else {
            page = nomencladorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nomencladors/:id} : get the "id" nomenclador.
     *
     * @param id the id of the nomencladorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nomencladorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NomencladorDTO> getNomenclador(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Nomenclador : {}", id);
        Optional<NomencladorDTO> nomencladorDTO = nomencladorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nomencladorDTO);
    }

    /**
     * {@code DELETE  /nomencladors/:id} : delete the "id" nomenclador.
     *
     * @param id the id of the nomencladorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNomenclador(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Nomenclador : {}", id);
        nomencladorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
