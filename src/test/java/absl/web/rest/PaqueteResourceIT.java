package absl.web.rest;

import static absl.domain.PaqueteAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Paquete;
import absl.domain.PlanMutual;
import absl.domain.enumeration.EstadoPaquete;
import absl.domain.enumeration.TipoIva;
import absl.repository.PaqueteRepository;
import absl.service.PaqueteService;
import absl.service.dto.PaqueteDTO;
import absl.service.mapper.PaqueteMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
 * Integration tests for the {@link PaqueteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaqueteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PERIODO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODO = LocalDate.parse("2023-12-11");

    private static final EstadoPaquete DEFAULT_ESTADO = EstadoPaquete.EN_CARGA;
    private static final EstadoPaquete UPDATED_ESTADO = EstadoPaquete.FP_GENERADO;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final TipoIva DEFAULT_TIPO_IVA = TipoIva.RESP_NO_INSCRIPTO;
    private static final TipoIva UPDATED_TIPO_IVA = TipoIva.RESP_INSCRIPTO;

    private static final String ENTITY_API_URL = "/api/paquetes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaqueteRepository paqueteRepository;

    @Mock
    private PaqueteRepository paqueteRepositoryMock;

    @Autowired
    private PaqueteMapper paqueteMapper;

    @Mock
    private PaqueteService paqueteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaqueteMockMvc;

    private Paquete paquete;

    private Paquete insertedPaquete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paquete createEntity(EntityManager em) {
        Paquete paquete = new Paquete()
            .nombre(DEFAULT_NOMBRE)
            .periodo(DEFAULT_PERIODO)
            .estado(DEFAULT_ESTADO)
            .descripcion(DEFAULT_DESCRIPCION)
            .tipoIva(DEFAULT_TIPO_IVA);
        // Add required entity
        PlanMutual planMutual;
        if (TestUtil.findAll(em, PlanMutual.class).isEmpty()) {
            planMutual = PlanMutualResourceIT.createEntity(em);
            em.persist(planMutual);
            em.flush();
        } else {
            planMutual = TestUtil.findAll(em, PlanMutual.class).get(0);
        }
        paquete.setPlan(planMutual);
        return paquete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paquete createUpdatedEntity(EntityManager em) {
        Paquete updatedPaquete = new Paquete()
            .nombre(UPDATED_NOMBRE)
            .periodo(UPDATED_PERIODO)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION)
            .tipoIva(UPDATED_TIPO_IVA);
        // Add required entity
        PlanMutual planMutual;
        if (TestUtil.findAll(em, PlanMutual.class).isEmpty()) {
            planMutual = PlanMutualResourceIT.createUpdatedEntity(em);
            em.persist(planMutual);
            em.flush();
        } else {
            planMutual = TestUtil.findAll(em, PlanMutual.class).get(0);
        }
        updatedPaquete.setPlan(planMutual);
        return updatedPaquete;
    }

    @BeforeEach
    void initTest() {
        paquete = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPaquete != null) {
            paqueteRepository.delete(insertedPaquete);
            insertedPaquete = null;
        }
    }

    @Test
    @Transactional
    void createPaquete() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);
        var returnedPaqueteDTO = om.readValue(
            restPaqueteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paqueteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaqueteDTO.class
        );

        // Validate the Paquete in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaquete = paqueteMapper.toEntity(returnedPaqueteDTO);
        assertPaqueteUpdatableFieldsEquals(returnedPaquete, getPersistedPaquete(returnedPaquete));

        insertedPaquete = returnedPaquete;
    }

    @Test
    @Transactional
    void createPaqueteWithExistingId() throws Exception {
        // Create the Paquete with an existing ID
        paquete.setId(1L);
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaqueteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paqueteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPeriodoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paquete.setPeriodo(null);

        // Create the Paquete, which fails.
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        restPaqueteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paqueteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaquetes() throws Exception {
        // Initialize the database
        insertedPaquete = paqueteRepository.saveAndFlush(paquete);

        // Get all the paqueteList
        restPaqueteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paquete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].periodo").value(hasItem(DEFAULT_PERIODO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].tipoIva").value(hasItem(DEFAULT_TIPO_IVA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaquetesWithEagerRelationshipsIsEnabled() throws Exception {
        when(paqueteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaqueteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paqueteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaquetesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paqueteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaqueteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paqueteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPaquete() throws Exception {
        // Initialize the database
        insertedPaquete = paqueteRepository.saveAndFlush(paquete);

        // Get the paquete
        restPaqueteMockMvc
            .perform(get(ENTITY_API_URL_ID, paquete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paquete.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.periodo").value(DEFAULT_PERIODO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.tipoIva").value(DEFAULT_TIPO_IVA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaquete() throws Exception {
        // Get the paquete
        restPaqueteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaquete() throws Exception {
        // Initialize the database
        insertedPaquete = paqueteRepository.saveAndFlush(paquete);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paquete
        Paquete updatedPaquete = paqueteRepository.findById(paquete.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaquete are not directly saved in db
        em.detach(updatedPaquete);
        updatedPaquete
            .nombre(UPDATED_NOMBRE)
            .periodo(UPDATED_PERIODO)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION)
            .tipoIva(UPDATED_TIPO_IVA);
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(updatedPaquete);

        restPaqueteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paqueteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paqueteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaqueteToMatchAllProperties(updatedPaquete);
    }

    @Test
    @Transactional
    void putNonExistingPaquete() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paquete.setId(longCount.incrementAndGet());

        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paqueteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paqueteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaquete() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paquete.setId(longCount.incrementAndGet());

        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paqueteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaquete() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paquete.setId(longCount.incrementAndGet());

        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paqueteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaqueteWithPatch() throws Exception {
        // Initialize the database
        insertedPaquete = paqueteRepository.saveAndFlush(paquete);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paquete using partial update
        Paquete partialUpdatedPaquete = new Paquete();
        partialUpdatedPaquete.setId(paquete.getId());

        partialUpdatedPaquete.descripcion(UPDATED_DESCRIPCION);

        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaquete.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaquete))
            )
            .andExpect(status().isOk());

        // Validate the Paquete in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaqueteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaquete, paquete), getPersistedPaquete(paquete));
    }

    @Test
    @Transactional
    void fullUpdatePaqueteWithPatch() throws Exception {
        // Initialize the database
        insertedPaquete = paqueteRepository.saveAndFlush(paquete);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paquete using partial update
        Paquete partialUpdatedPaquete = new Paquete();
        partialUpdatedPaquete.setId(paquete.getId());

        partialUpdatedPaquete
            .nombre(UPDATED_NOMBRE)
            .periodo(UPDATED_PERIODO)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION)
            .tipoIva(UPDATED_TIPO_IVA);

        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaquete.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaquete))
            )
            .andExpect(status().isOk());

        // Validate the Paquete in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaqueteUpdatableFieldsEquals(partialUpdatedPaquete, getPersistedPaquete(partialUpdatedPaquete));
    }

    @Test
    @Transactional
    void patchNonExistingPaquete() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paquete.setId(longCount.incrementAndGet());

        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paqueteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paqueteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaquete() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paquete.setId(longCount.incrementAndGet());

        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paqueteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaquete() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paquete.setId(longCount.incrementAndGet());

        // Create the Paquete
        PaqueteDTO paqueteDTO = paqueteMapper.toDto(paquete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaqueteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paqueteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paquete in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaquete() throws Exception {
        // Initialize the database
        insertedPaquete = paqueteRepository.saveAndFlush(paquete);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paquete
        restPaqueteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paquete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paqueteRepository.count();
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

    protected Paquete getPersistedPaquete(Paquete paquete) {
        return paqueteRepository.findById(paquete.getId()).orElseThrow();
    }

    protected void assertPersistedPaqueteToMatchAllProperties(Paquete expectedPaquete) {
        assertPaqueteAllPropertiesEquals(expectedPaquete, getPersistedPaquete(expectedPaquete));
    }

    protected void assertPersistedPaqueteToMatchUpdatableProperties(Paquete expectedPaquete) {
        assertPaqueteAllUpdatablePropertiesEquals(expectedPaquete, getPersistedPaquete(expectedPaquete));
    }
}
