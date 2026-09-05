package absl.web.rest;

import static absl.domain.PlanMutualAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Mutual;
import absl.domain.PlanMutual;
import absl.repository.PlanMutualRepository;
import absl.service.PlanMutualService;
import absl.service.dto.PlanMutualDTO;
import absl.service.mapper.PlanMutualMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PlanMutualResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanMutualResourceIT {

    private static final String DEFAULT_CATEGORIA = "AAAAAAA";
    private static final String UPDATED_CATEGORIA = "BBBBBBB";

    private static final String DEFAULT_ETIQUETA_REPORTE = "AAAAAAA";
    private static final String UPDATED_ETIQUETA_REPORTE = "BBBBBBB";

    private static final Boolean DEFAULT_PRESTACIONES_IMPORTADAS = false;
    private static final Boolean UPDATED_PRESTACIONES_IMPORTADAS = true;

    private static final String ENTITY_API_URL = "/api/plan-mutuals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanMutualRepository planMutualRepository;

    @Mock
    private PlanMutualRepository planMutualRepositoryMock;

    @Autowired
    private PlanMutualMapper planMutualMapper;

    @Mock
    private PlanMutualService planMutualServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanMutualMockMvc;

    private PlanMutual planMutual;

    private PlanMutual insertedPlanMutual;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanMutual createEntity(EntityManager em) {
        PlanMutual planMutual = new PlanMutual()
            .categoria(DEFAULT_CATEGORIA)
            .etiquetaReporte(DEFAULT_ETIQUETA_REPORTE)
            .prestacionesImportadas(DEFAULT_PRESTACIONES_IMPORTADAS);
        // Add required entity
        Mutual mutual;
        if (TestUtil.findAll(em, Mutual.class).isEmpty()) {
            mutual = MutualResourceIT.createEntity();
            em.persist(mutual);
            em.flush();
        } else {
            mutual = TestUtil.findAll(em, Mutual.class).get(0);
        }
        planMutual.setMutual(mutual);
        return planMutual;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanMutual createUpdatedEntity(EntityManager em) {
        PlanMutual updatedPlanMutual = new PlanMutual()
            .categoria(UPDATED_CATEGORIA)
            .etiquetaReporte(UPDATED_ETIQUETA_REPORTE)
            .prestacionesImportadas(UPDATED_PRESTACIONES_IMPORTADAS);
        // Add required entity
        Mutual mutual;
        if (TestUtil.findAll(em, Mutual.class).isEmpty()) {
            mutual = MutualResourceIT.createUpdatedEntity();
            em.persist(mutual);
            em.flush();
        } else {
            mutual = TestUtil.findAll(em, Mutual.class).get(0);
        }
        updatedPlanMutual.setMutual(mutual);
        return updatedPlanMutual;
    }

    @BeforeEach
    void initTest() {
        planMutual = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPlanMutual != null) {
            planMutualRepository.delete(insertedPlanMutual);
            insertedPlanMutual = null;
        }
    }

    @Test
    @Transactional
    void createPlanMutual() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);
        var returnedPlanMutualDTO = om.readValue(
            restPlanMutualMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planMutualDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanMutualDTO.class
        );

        // Validate the PlanMutual in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlanMutual = planMutualMapper.toEntity(returnedPlanMutualDTO);
        assertPlanMutualUpdatableFieldsEquals(returnedPlanMutual, getPersistedPlanMutual(returnedPlanMutual));

        insertedPlanMutual = returnedPlanMutual;
    }

    @Test
    @Transactional
    void createPlanMutualWithExistingId() throws Exception {
        // Create the PlanMutual with an existing ID
        planMutual.setId(1L);
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planMutualDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoriaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planMutual.setCategoria(null);

        // Create the PlanMutual, which fails.
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        restPlanMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planMutualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEtiquetaReporteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planMutual.setEtiquetaReporte(null);

        // Create the PlanMutual, which fails.
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        restPlanMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planMutualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanMutuals() throws Exception {
        // Initialize the database
        insertedPlanMutual = planMutualRepository.saveAndFlush(planMutual);

        // Get all the planMutualList
        restPlanMutualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planMutual.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA)))
            .andExpect(jsonPath("$.[*].etiquetaReporte").value(hasItem(DEFAULT_ETIQUETA_REPORTE)))
            .andExpect(jsonPath("$.[*].prestacionesImportadas").value(hasItem(DEFAULT_PRESTACIONES_IMPORTADAS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanMutualsWithEagerRelationshipsIsEnabled() throws Exception {
        when(planMutualServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanMutualMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planMutualServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanMutualsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planMutualServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanMutualMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(planMutualRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlanMutual() throws Exception {
        // Initialize the database
        insertedPlanMutual = planMutualRepository.saveAndFlush(planMutual);

        // Get the planMutual
        restPlanMutualMockMvc
            .perform(get(ENTITY_API_URL_ID, planMutual.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planMutual.getId().intValue()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA))
            .andExpect(jsonPath("$.etiquetaReporte").value(DEFAULT_ETIQUETA_REPORTE))
            .andExpect(jsonPath("$.prestacionesImportadas").value(DEFAULT_PRESTACIONES_IMPORTADAS));
    }

    @Test
    @Transactional
    void getNonExistingPlanMutual() throws Exception {
        // Get the planMutual
        restPlanMutualMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanMutual() throws Exception {
        // Initialize the database
        insertedPlanMutual = planMutualRepository.saveAndFlush(planMutual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planMutual
        PlanMutual updatedPlanMutual = planMutualRepository.findById(planMutual.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanMutual are not directly saved in db
        em.detach(updatedPlanMutual);
        updatedPlanMutual
            .categoria(UPDATED_CATEGORIA)
            .etiquetaReporte(UPDATED_ETIQUETA_REPORTE)
            .prestacionesImportadas(UPDATED_PRESTACIONES_IMPORTADAS);
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(updatedPlanMutual);

        restPlanMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planMutualDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planMutualDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanMutualToMatchAllProperties(updatedPlanMutual);
    }

    @Test
    @Transactional
    void putNonExistingPlanMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planMutual.setId(longCount.incrementAndGet());

        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planMutualDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planMutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planMutual.setId(longCount.incrementAndGet());

        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planMutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planMutual.setId(longCount.incrementAndGet());

        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMutualMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planMutualDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanMutualWithPatch() throws Exception {
        // Initialize the database
        insertedPlanMutual = planMutualRepository.saveAndFlush(planMutual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planMutual using partial update
        PlanMutual partialUpdatedPlanMutual = new PlanMutual();
        partialUpdatedPlanMutual.setId(planMutual.getId());

        partialUpdatedPlanMutual.etiquetaReporte(UPDATED_ETIQUETA_REPORTE);

        restPlanMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanMutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanMutual))
            )
            .andExpect(status().isOk());

        // Validate the PlanMutual in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanMutualUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlanMutual, planMutual),
            getPersistedPlanMutual(planMutual)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlanMutualWithPatch() throws Exception {
        // Initialize the database
        insertedPlanMutual = planMutualRepository.saveAndFlush(planMutual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planMutual using partial update
        PlanMutual partialUpdatedPlanMutual = new PlanMutual();
        partialUpdatedPlanMutual.setId(planMutual.getId());

        partialUpdatedPlanMutual
            .categoria(UPDATED_CATEGORIA)
            .etiquetaReporte(UPDATED_ETIQUETA_REPORTE)
            .prestacionesImportadas(UPDATED_PRESTACIONES_IMPORTADAS);

        restPlanMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanMutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanMutual))
            )
            .andExpect(status().isOk());

        // Validate the PlanMutual in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanMutualUpdatableFieldsEquals(partialUpdatedPlanMutual, getPersistedPlanMutual(partialUpdatedPlanMutual));
    }

    @Test
    @Transactional
    void patchNonExistingPlanMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planMutual.setId(longCount.incrementAndGet());

        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planMutualDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planMutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planMutual.setId(longCount.incrementAndGet());

        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planMutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planMutual.setId(longCount.incrementAndGet());

        // Create the PlanMutual
        PlanMutualDTO planMutualDTO = planMutualMapper.toDto(planMutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanMutualMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planMutualDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanMutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanMutual() throws Exception {
        // Initialize the database
        insertedPlanMutual = planMutualRepository.saveAndFlush(planMutual);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planMutual
        restPlanMutualMockMvc
            .perform(delete(ENTITY_API_URL_ID, planMutual.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planMutualRepository.count();
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

    protected PlanMutual getPersistedPlanMutual(PlanMutual planMutual) {
        return planMutualRepository.findById(planMutual.getId()).orElseThrow();
    }

    protected void assertPersistedPlanMutualToMatchAllProperties(PlanMutual expectedPlanMutual) {
        assertPlanMutualAllPropertiesEquals(expectedPlanMutual, getPersistedPlanMutual(expectedPlanMutual));
    }

    protected void assertPersistedPlanMutualToMatchUpdatableProperties(PlanMutual expectedPlanMutual) {
        assertPlanMutualAllUpdatablePropertiesEquals(expectedPlanMutual, getPersistedPlanMutual(expectedPlanMutual));
    }
}
