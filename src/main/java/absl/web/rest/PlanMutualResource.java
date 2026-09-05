package absl.web.rest;

import absl.repository.PlanMutualRepository;
import absl.service.PlanMutualService;
import absl.service.dto.PlanMutualDTO;
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
 * REST controller for managing {@link absl.domain.PlanMutual}.
 */
@RestController
@RequestMapping("/api/plan-mutuals")
public class PlanMutualResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanMutualResource.class);

    private static final String ENTITY_NAME = "planMutual";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final PlanMutualService planMutualService;

    private final PlanMutualRepository planMutualRepository;

    public PlanMutualResource(PlanMutualService planMutualService, PlanMutualRepository planMutualRepository) {
        this.planMutualService = planMutualService;
        this.planMutualRepository = planMutualRepository;
    }

    /**
     * {@code POST  /plan-mutuals} : Create a new planMutual.
     *
     * @param planMutualDTO the planMutualDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planMutualDTO, or with status {@code 400 (Bad Request)} if the planMutual has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanMutualDTO> createPlanMutual(@Valid @RequestBody PlanMutualDTO planMutualDTO) throws URISyntaxException {
        LOG.debug("REST request to save PlanMutual : {}", planMutualDTO);
        if (planMutualDTO.getId() != null) {
            throw new BadRequestAlertException("A new planMutual cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planMutualDTO = planMutualService.save(planMutualDTO);
        return ResponseEntity.created(new URI("/api/plan-mutuals/" + planMutualDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, planMutualDTO.getId().toString()))
            .body(planMutualDTO);
    }

    /**
     * {@code PUT  /plan-mutuals/:id} : Updates an existing planMutual.
     *
     * @param id the id of the planMutualDTO to save.
     * @param planMutualDTO the planMutualDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planMutualDTO,
     * or with status {@code 400 (Bad Request)} if the planMutualDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planMutualDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanMutualDTO> updatePlanMutual(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanMutualDTO planMutualDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlanMutual : {}, {}", id, planMutualDTO);
        if (planMutualDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planMutualDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planMutualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planMutualDTO = planMutualService.update(planMutualDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planMutualDTO.getId().toString()))
            .body(planMutualDTO);
    }

    /**
     * {@code PATCH  /plan-mutuals/:id} : Partial updates given fields of an existing planMutual, field will ignore if it is null
     *
     * @param id the id of the planMutualDTO to save.
     * @param planMutualDTO the planMutualDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planMutualDTO,
     * or with status {@code 400 (Bad Request)} if the planMutualDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planMutualDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planMutualDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanMutualDTO> partialUpdatePlanMutual(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanMutualDTO planMutualDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlanMutual partially : {}, {}", id, planMutualDTO);
        if (planMutualDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planMutualDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planMutualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanMutualDTO> result = planMutualService.partialUpdate(planMutualDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, planMutualDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /plan-mutuals} : get all the Plan Mutuals.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Plan Mutuals in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlanMutualDTO>> getAllPlanMutuals(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PlanMutuals");
        Page<PlanMutualDTO> page;
        if (eagerload) {
            page = planMutualService.findAllWithEagerRelationships(pageable);
        } else {
            page = planMutualService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plan-mutuals/:id} : get the "id" planMutual.
     *
     * @param id the id of the planMutualDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planMutualDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanMutualDTO> getPlanMutual(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlanMutual : {}", id);
        Optional<PlanMutualDTO> planMutualDTO = planMutualService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planMutualDTO);
    }

    /**
     * {@code DELETE  /plan-mutuals/:id} : delete the "id" planMutual.
     *
     * @param id the id of the planMutualDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanMutual(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlanMutual : {}", id);
        planMutualService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
