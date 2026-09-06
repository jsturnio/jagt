package absl.web.rest;

import static absl.domain.PracticaAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static absl.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Orden;
import absl.domain.Practica;
import absl.domain.Prestacion;
import absl.repository.PracticaRepository;
import absl.service.PracticaService;
import absl.service.dto.PracticaDTO;
import absl.service.mapper.PracticaMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PracticaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PracticaResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final BigDecimal DEFAULT_PVALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_PVALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_PVALOR = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/practicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PracticaRepository practicaRepository;

    @Mock
    private PracticaRepository practicaRepositoryMock;

    @Autowired
    private PracticaMapper practicaMapper;

    @Mock
    private PracticaService practicaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPracticaMockMvc;

    private Practica practica;

    private Practica insertedPractica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Practica createEntity(EntityManager em) {
        Practica practica = new Practica().cantidad(DEFAULT_CANTIDAD).pvalor(DEFAULT_PVALOR);
        // Add required entity
        Prestacion prestacion;
        if (TestUtil.findAll(em, Prestacion.class).isEmpty()) {
            prestacion = PrestacionResourceIT.createEntity(em);
            em.persist(prestacion);
            em.flush();
        } else {
            prestacion = TestUtil.findAll(em, Prestacion.class).get(0);
        }
        practica.setPrestacion(prestacion);
        // Add required entity
        Orden orden;
        if (TestUtil.findAll(em, Orden.class).isEmpty()) {
            orden = OrdenResourceIT.createEntity(em);
            em.persist(orden);
            em.flush();
        } else {
            orden = TestUtil.findAll(em, Orden.class).get(0);
        }
        practica.setOrden(orden);
        return practica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Practica createUpdatedEntity(EntityManager em) {
        Practica updatedPractica = new Practica().cantidad(UPDATED_CANTIDAD).pvalor(UPDATED_PVALOR);
        // Add required entity
        Prestacion prestacion;
        if (TestUtil.findAll(em, Prestacion.class).isEmpty()) {
            prestacion = PrestacionResourceIT.createUpdatedEntity(em);
            em.persist(prestacion);
            em.flush();
        } else {
            prestacion = TestUtil.findAll(em, Prestacion.class).get(0);
        }
        updatedPractica.setPrestacion(prestacion);
        // Add required entity
        Orden orden;
        if (TestUtil.findAll(em, Orden.class).isEmpty()) {
            orden = OrdenResourceIT.createUpdatedEntity(em);
            em.persist(orden);
            em.flush();
        } else {
            orden = TestUtil.findAll(em, Orden.class).get(0);
        }
        updatedPractica.setOrden(orden);
        return updatedPractica;
    }

    @BeforeEach
    void initTest() {
        practica = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPractica != null) {
            practicaRepository.delete(insertedPractica);
            insertedPractica = null;
        }
    }

    @Test
    @Transactional
    void createPractica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);
        var returnedPracticaDTO = om.readValue(
            restPracticaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practicaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PracticaDTO.class
        );

        // Validate the Practica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPractica = practicaMapper.toEntity(returnedPracticaDTO);
        assertPracticaUpdatableFieldsEquals(returnedPractica, getPersistedPractica(returnedPractica));

        insertedPractica = returnedPractica;
    }

    @Test
    @Transactional
    void createPracticaWithExistingId() throws Exception {
        // Create the Practica with an existing ID
        practica.setId(1L);
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPracticaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practicaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        practica.setCantidad(null);

        // Create the Practica, which fails.
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        restPracticaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPracticas() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList
        restPracticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(practica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].pvalor").value(hasItem(sameNumber(DEFAULT_PVALOR))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPracticasWithEagerRelationshipsIsEnabled() throws Exception {
        when(practicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPracticaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(practicaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPracticasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(practicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPracticaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(practicaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPractica() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get the practica
        restPracticaMockMvc
            .perform(get(ENTITY_API_URL_ID, practica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(practica.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.pvalor").value(sameNumber(DEFAULT_PVALOR)));
    }

    @Test
    @Transactional
    void getPracticasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        Long id = practica.getId();

        defaultPracticaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPracticaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPracticaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad equals to
        defaultPracticaFiltering("cantidad.equals=" + DEFAULT_CANTIDAD, "cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad in
        defaultPracticaFiltering("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD, "cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad is not null
        defaultPracticaFiltering("cantidad.specified=true", "cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad is greater than or equal to
        defaultPracticaFiltering("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad is less than or equal to
        defaultPracticaFiltering("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad is less than
        defaultPracticaFiltering("cantidad.lessThan=" + UPDATED_CANTIDAD, "cantidad.lessThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPracticasByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where cantidad is greater than
        defaultPracticaFiltering("cantidad.greaterThan=" + SMALLER_CANTIDAD, "cantidad.greaterThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor equals to
        defaultPracticaFiltering("pvalor.equals=" + DEFAULT_PVALOR, "pvalor.equals=" + UPDATED_PVALOR);
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor in
        defaultPracticaFiltering("pvalor.in=" + DEFAULT_PVALOR + "," + UPDATED_PVALOR, "pvalor.in=" + UPDATED_PVALOR);
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor is not null
        defaultPracticaFiltering("pvalor.specified=true", "pvalor.specified=false");
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor is greater than or equal to
        defaultPracticaFiltering("pvalor.greaterThanOrEqual=" + DEFAULT_PVALOR, "pvalor.greaterThanOrEqual=" + UPDATED_PVALOR);
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor is less than or equal to
        defaultPracticaFiltering("pvalor.lessThanOrEqual=" + DEFAULT_PVALOR, "pvalor.lessThanOrEqual=" + SMALLER_PVALOR);
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor is less than
        defaultPracticaFiltering("pvalor.lessThan=" + UPDATED_PVALOR, "pvalor.lessThan=" + DEFAULT_PVALOR);
    }

    @Test
    @Transactional
    void getAllPracticasByPvalorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        // Get all the practicaList where pvalor is greater than
        defaultPracticaFiltering("pvalor.greaterThan=" + SMALLER_PVALOR, "pvalor.greaterThan=" + DEFAULT_PVALOR);
    }

    @Test
    @Transactional
    void getAllPracticasByPrestacionIsEqualToSomething() throws Exception {
        Prestacion prestacion;
        if (TestUtil.findAll(em, Prestacion.class).isEmpty()) {
            practicaRepository.saveAndFlush(practica);
            prestacion = PrestacionResourceIT.createEntity(em);
        } else {
            prestacion = TestUtil.findAll(em, Prestacion.class).get(0);
        }
        em.persist(prestacion);
        em.flush();
        practica.setPrestacion(prestacion);
        practicaRepository.saveAndFlush(practica);
        Long prestacionId = prestacion.getId();
        // Get all the practicaList where prestacion equals to prestacionId
        defaultPracticaShouldBeFound("prestacionId.equals=" + prestacionId);

        // Get all the practicaList where prestacion equals to (prestacionId + 1)
        defaultPracticaShouldNotBeFound("prestacionId.equals=" + (prestacionId + 1));
    }

    @Test
    @Transactional
    void getAllPracticasByOrdenIsEqualToSomething() throws Exception {
        Orden orden;
        if (TestUtil.findAll(em, Orden.class).isEmpty()) {
            practicaRepository.saveAndFlush(practica);
            orden = OrdenResourceIT.createEntity(em);
        } else {
            orden = TestUtil.findAll(em, Orden.class).get(0);
        }
        em.persist(orden);
        em.flush();
        practica.setOrden(orden);
        practicaRepository.saveAndFlush(practica);
        Long ordenId = orden.getId();
        // Get all the practicaList where orden equals to ordenId
        defaultPracticaShouldBeFound("ordenId.equals=" + ordenId);

        // Get all the practicaList where orden equals to (ordenId + 1)
        defaultPracticaShouldNotBeFound("ordenId.equals=" + (ordenId + 1));
    }

    private void defaultPracticaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPracticaShouldBeFound(shouldBeFound);
        defaultPracticaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPracticaShouldBeFound(String filter) throws Exception {
        restPracticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(practica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].pvalor").value(hasItem(sameNumber(DEFAULT_PVALOR))));

        // Check, that the count call also returns 1
        restPracticaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPracticaShouldNotBeFound(String filter) throws Exception {
        restPracticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPracticaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPractica() throws Exception {
        // Get the practica
        restPracticaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPractica() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the practica
        Practica updatedPractica = practicaRepository.findById(practica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPractica are not directly saved in db
        em.detach(updatedPractica);
        updatedPractica.cantidad(UPDATED_CANTIDAD).pvalor(UPDATED_PVALOR);
        PracticaDTO practicaDTO = practicaMapper.toDto(updatedPractica);

        restPracticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, practicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(practicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPracticaToMatchAllProperties(updatedPractica);
    }

    @Test
    @Transactional
    void putNonExistingPractica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practica.setId(longCount.incrementAndGet());

        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPracticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, practicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(practicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPractica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practica.setId(longCount.incrementAndGet());

        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(practicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPractica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practica.setId(longCount.incrementAndGet());

        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(practicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePracticaWithPatch() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the practica using partial update
        Practica partialUpdatedPractica = new Practica();
        partialUpdatedPractica.setId(practica.getId());

        partialUpdatedPractica.cantidad(UPDATED_CANTIDAD).pvalor(UPDATED_PVALOR);

        restPracticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPractica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPractica))
            )
            .andExpect(status().isOk());

        // Validate the Practica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPracticaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPractica, practica), getPersistedPractica(practica));
    }

    @Test
    @Transactional
    void fullUpdatePracticaWithPatch() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the practica using partial update
        Practica partialUpdatedPractica = new Practica();
        partialUpdatedPractica.setId(practica.getId());

        partialUpdatedPractica.cantidad(UPDATED_CANTIDAD).pvalor(UPDATED_PVALOR);

        restPracticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPractica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPractica))
            )
            .andExpect(status().isOk());

        // Validate the Practica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPracticaUpdatableFieldsEquals(partialUpdatedPractica, getPersistedPractica(partialUpdatedPractica));
    }

    @Test
    @Transactional
    void patchNonExistingPractica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practica.setId(longCount.incrementAndGet());

        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPracticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, practicaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(practicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPractica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practica.setId(longCount.incrementAndGet());

        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(practicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPractica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        practica.setId(longCount.incrementAndGet());

        // Create the Practica
        PracticaDTO practicaDTO = practicaMapper.toDto(practica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPracticaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(practicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Practica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePractica() throws Exception {
        // Initialize the database
        insertedPractica = practicaRepository.saveAndFlush(practica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the practica
        restPracticaMockMvc
            .perform(delete(ENTITY_API_URL_ID, practica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return practicaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Practica getPersistedPractica(Practica practica) {
        return practicaRepository.findById(practica.getId()).orElseThrow();
    }

    protected void assertPersistedPracticaToMatchAllProperties(Practica expectedPractica) {
        assertPracticaAllPropertiesEquals(expectedPractica, getPersistedPractica(expectedPractica));
    }

    protected void assertPersistedPracticaToMatchUpdatableProperties(Practica expectedPractica) {
        assertPracticaAllUpdatablePropertiesEquals(expectedPractica, getPersistedPractica(expectedPractica));
    }
}
