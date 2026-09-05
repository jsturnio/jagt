package absl.web.rest;

import static absl.domain.PrestacionAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static absl.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Nomenclador;
import absl.domain.Prestacion;
import absl.repository.PrestacionRepository;
import absl.service.PrestacionService;
import absl.service.dto.PrestacionDTO;
import absl.service.mapper.PrestacionMapper;
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
 * Integration tests for the {@link PrestacionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrestacionResourceIT {

    private static final Integer DEFAULT_CODIGO = 1;
    private static final Integer UPDATED_CODIGO = 2;

    private static final String DEFAULT_CODIGO_INOS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_INOS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_UB = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_UB = new BigDecimal(2);

    private static final Boolean DEFAULT_REQ_AUTORIZACION = false;
    private static final Boolean UPDATED_REQ_AUTORIZACION = true;

    private static final String ENTITY_API_URL = "/api/prestacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrestacionRepository prestacionRepository;

    @Mock
    private PrestacionRepository prestacionRepositoryMock;

    @Autowired
    private PrestacionMapper prestacionMapper;

    @Mock
    private PrestacionService prestacionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestacionMockMvc;

    private Prestacion prestacion;

    private Prestacion insertedPrestacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestacion createEntity(EntityManager em) {
        Prestacion prestacion = new Prestacion()
            .codigo(DEFAULT_CODIGO)
            .codigoInos(DEFAULT_CODIGO_INOS)
            .valorUb(DEFAULT_VALOR_UB)
            .reqAutorizacion(DEFAULT_REQ_AUTORIZACION);
        // Add required entity
        Nomenclador nomenclador;
        if (TestUtil.findAll(em, Nomenclador.class).isEmpty()) {
            nomenclador = NomencladorResourceIT.createEntity(em);
            em.persist(nomenclador);
            em.flush();
        } else {
            nomenclador = TestUtil.findAll(em, Nomenclador.class).get(0);
        }
        prestacion.setNomenclador(nomenclador);
        return prestacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestacion createUpdatedEntity(EntityManager em) {
        Prestacion updatedPrestacion = new Prestacion()
            .codigo(UPDATED_CODIGO)
            .codigoInos(UPDATED_CODIGO_INOS)
            .valorUb(UPDATED_VALOR_UB)
            .reqAutorizacion(UPDATED_REQ_AUTORIZACION);
        // Add required entity
        Nomenclador nomenclador;
        if (TestUtil.findAll(em, Nomenclador.class).isEmpty()) {
            nomenclador = NomencladorResourceIT.createUpdatedEntity(em);
            em.persist(nomenclador);
            em.flush();
        } else {
            nomenclador = TestUtil.findAll(em, Nomenclador.class).get(0);
        }
        updatedPrestacion.setNomenclador(nomenclador);
        return updatedPrestacion;
    }

    @BeforeEach
    void initTest() {
        prestacion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPrestacion != null) {
            prestacionRepository.delete(insertedPrestacion);
            insertedPrestacion = null;
        }
    }

    @Test
    @Transactional
    void createPrestacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);
        var returnedPrestacionDTO = om.readValue(
            restPrestacionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestacionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrestacionDTO.class
        );

        // Validate the Prestacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrestacion = prestacionMapper.toEntity(returnedPrestacionDTO);
        assertPrestacionUpdatableFieldsEquals(returnedPrestacion, getPersistedPrestacion(returnedPrestacion));

        insertedPrestacion = returnedPrestacion;
    }

    @Test
    @Transactional
    void createPrestacionWithExistingId() throws Exception {
        // Create the Prestacion with an existing ID
        prestacion.setId(1L);
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        prestacion.setCodigo(null);

        // Create the Prestacion, which fails.
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        restPrestacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrestacions() throws Exception {
        // Initialize the database
        insertedPrestacion = prestacionRepository.saveAndFlush(prestacion);

        // Get all the prestacionList
        restPrestacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].codigoInos").value(hasItem(DEFAULT_CODIGO_INOS)))
            .andExpect(jsonPath("$.[*].valorUb").value(hasItem(sameNumber(DEFAULT_VALOR_UB))))
            .andExpect(jsonPath("$.[*].reqAutorizacion").value(hasItem(DEFAULT_REQ_AUTORIZACION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestacionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prestacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prestacionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestacionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prestacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrestacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prestacionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrestacion() throws Exception {
        // Initialize the database
        insertedPrestacion = prestacionRepository.saveAndFlush(prestacion);

        // Get the prestacion
        restPrestacionMockMvc
            .perform(get(ENTITY_API_URL_ID, prestacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestacion.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.codigoInos").value(DEFAULT_CODIGO_INOS))
            .andExpect(jsonPath("$.valorUb").value(sameNumber(DEFAULT_VALOR_UB)))
            .andExpect(jsonPath("$.reqAutorizacion").value(DEFAULT_REQ_AUTORIZACION));
    }

    @Test
    @Transactional
    void getNonExistingPrestacion() throws Exception {
        // Get the prestacion
        restPrestacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestacion() throws Exception {
        // Initialize the database
        insertedPrestacion = prestacionRepository.saveAndFlush(prestacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestacion
        Prestacion updatedPrestacion = prestacionRepository.findById(prestacion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrestacion are not directly saved in db
        em.detach(updatedPrestacion);
        updatedPrestacion
            .codigo(UPDATED_CODIGO)
            .codigoInos(UPDATED_CODIGO_INOS)
            .valorUb(UPDATED_VALOR_UB)
            .reqAutorizacion(UPDATED_REQ_AUTORIZACION);
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(updatedPrestacion);

        restPrestacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrestacionToMatchAllProperties(updatedPrestacion);
    }

    @Test
    @Transactional
    void putNonExistingPrestacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestacion.setId(longCount.incrementAndGet());

        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestacion.setId(longCount.incrementAndGet());

        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestacion.setId(longCount.incrementAndGet());

        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestacionWithPatch() throws Exception {
        // Initialize the database
        insertedPrestacion = prestacionRepository.saveAndFlush(prestacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestacion using partial update
        Prestacion partialUpdatedPrestacion = new Prestacion();
        partialUpdatedPrestacion.setId(prestacion.getId());

        partialUpdatedPrestacion.codigo(UPDATED_CODIGO).codigoInos(UPDATED_CODIGO_INOS).reqAutorizacion(UPDATED_REQ_AUTORIZACION);

        restPrestacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestacion))
            )
            .andExpect(status().isOk());

        // Validate the Prestacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrestacion, prestacion),
            getPersistedPrestacion(prestacion)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrestacionWithPatch() throws Exception {
        // Initialize the database
        insertedPrestacion = prestacionRepository.saveAndFlush(prestacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestacion using partial update
        Prestacion partialUpdatedPrestacion = new Prestacion();
        partialUpdatedPrestacion.setId(prestacion.getId());

        partialUpdatedPrestacion
            .codigo(UPDATED_CODIGO)
            .codigoInos(UPDATED_CODIGO_INOS)
            .valorUb(UPDATED_VALOR_UB)
            .reqAutorizacion(UPDATED_REQ_AUTORIZACION);

        restPrestacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestacion))
            )
            .andExpect(status().isOk());

        // Validate the Prestacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestacionUpdatableFieldsEquals(partialUpdatedPrestacion, getPersistedPrestacion(partialUpdatedPrestacion));
    }

    @Test
    @Transactional
    void patchNonExistingPrestacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestacion.setId(longCount.incrementAndGet());

        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestacion.setId(longCount.incrementAndGet());

        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestacion.setId(longCount.incrementAndGet());

        // Create the Prestacion
        PrestacionDTO prestacionDTO = prestacionMapper.toDto(prestacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestacionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prestacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestacion() throws Exception {
        // Initialize the database
        insertedPrestacion = prestacionRepository.saveAndFlush(prestacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prestacion
        restPrestacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prestacionRepository.count();
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

    protected Prestacion getPersistedPrestacion(Prestacion prestacion) {
        return prestacionRepository.findById(prestacion.getId()).orElseThrow();
    }

    protected void assertPersistedPrestacionToMatchAllProperties(Prestacion expectedPrestacion) {
        assertPrestacionAllPropertiesEquals(expectedPrestacion, getPersistedPrestacion(expectedPrestacion));
    }

    protected void assertPersistedPrestacionToMatchUpdatableProperties(Prestacion expectedPrestacion) {
        assertPrestacionAllUpdatablePropertiesEquals(expectedPrestacion, getPersistedPrestacion(expectedPrestacion));
    }
}
