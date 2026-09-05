package absl.web.rest;

import absl.repository.EmpleadoRepository;
import absl.service.EmpleadoService;
import absl.service.dto.EmpleadoDTO;
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
 * REST controller for managing {@link absl.domain.Empleado}.
 */
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadoResource.class);

    private static final String ENTITY_NAME = "empleado";

    @Value("${jhipster.clientApp.name:jag}")
    private String applicationName;

    private final EmpleadoService empleadoService;

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoResource(EmpleadoService empleadoService, EmpleadoRepository empleadoRepository) {
        this.empleadoService = empleadoService;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * {@code POST  /empleados} : Create a new empleado.
     *
     * @param empleadoDTO the empleadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleadoDTO, or with status {@code 400 (Bad Request)} if the empleado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmpleadoDTO> createEmpleado(@Valid @RequestBody EmpleadoDTO empleadoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Empleado : {}", empleadoDTO);
        if (empleadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new empleado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        empleadoDTO = empleadoService.save(empleadoDTO);
        return ResponseEntity.created(new URI("/api/empleados/" + empleadoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, empleadoDTO.getId().toString()))
            .body(empleadoDTO);
    }

    /**
     * {@code PUT  /empleados/:id} : Updates an existing empleado.
     *
     * @param id the id of the empleadoDTO to save.
     * @param empleadoDTO the empleadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadoDTO,
     * or with status {@code 400 (Bad Request)} if the empleadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmpleadoDTO empleadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Empleado : {}, {}", id, empleadoDTO);
        if (empleadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        empleadoDTO = empleadoService.update(empleadoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, empleadoDTO.getId().toString()))
            .body(empleadoDTO);
    }

    /**
     * {@code PATCH  /empleados/:id} : Partial updates given fields of an existing empleado, field will ignore if it is null
     *
     * @param id the id of the empleadoDTO to save.
     * @param empleadoDTO the empleadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadoDTO,
     * or with status {@code 400 (Bad Request)} if the empleadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the empleadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmpleadoDTO> partialUpdateEmpleado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmpleadoDTO empleadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Empleado partially : {}, {}", id, empleadoDTO);
        if (empleadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empleadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmpleadoDTO> result = empleadoService.partialUpdate(empleadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, empleadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /empleados} : get all the Empleados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Empleados in body.
     */
    @GetMapping("")
    public List<EmpleadoDTO> getAllEmpleados(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Empleados");
        if (eagerload) {
            return empleadoService.findAllWithEagerRelationships();
        } else {
            return empleadoService.findAll();
        }
    }

    /**
     * {@code GET  /empleados/:id} : get the "id" empleado.
     *
     * @param id the id of the empleadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getEmpleado(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Empleado : {}", id);
        Optional<EmpleadoDTO> empleadoDTO = empleadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empleadoDTO);
    }

    /**
     * {@code DELETE  /empleados/:id} : delete the "id" empleado.
     *
     * @param id the id of the empleadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Empleado : {}", id);
        empleadoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
