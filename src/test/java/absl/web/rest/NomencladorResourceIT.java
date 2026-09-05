package absl.web.rest;

import static absl.domain.NomencladorAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Mutual;
import absl.domain.Nomenclador;
import absl.domain.enumeration.TipoNomenclador;
import absl.repository.NomencladorRepository;
import absl.service.NomencladorService;
import absl.service.dto.NomencladorDTO;
import absl.service.mapper.NomencladorMapper;
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
 * Integration tests for the {@link NomencladorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NomencladorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final TipoNomenclador DEFAULT_TIPO = TipoNomenclador.NBU;
    private static final TipoNomenclador UPDATED_TIPO = TipoNomenclador.INOS;

    private static final String ENTITY_API_URL = "/api/nomencladors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NomencladorRepository nomencladorRepository;

    @Mock
    private NomencladorRepository nomencladorRepositoryMock;

    @Autowired
    private NomencladorMapper nomencladorMapper;

    @Mock
    private NomencladorService nomencladorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNomencladorMockMvc;

    private Nomenclador nomenclador;

    private Nomenclador insertedNomenclador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nomenclador createEntity(EntityManager em) {
        Nomenclador nomenclador = new Nomenclador().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION).tipo(DEFAULT_TIPO);
        // Add required entity
        Mutual mutual;
        if (TestUtil.findAll(em, Mutual.class).isEmpty()) {
            mutual = MutualResourceIT.createEntity();
            em.persist(mutual);
            em.flush();
        } else {
            mutual = TestUtil.findAll(em, Mutual.class).get(0);
        }
        nomenclador.setMutual(mutual);
        return nomenclador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nomenclador createUpdatedEntity(EntityManager em) {
        Nomenclador updatedNomenclador = new Nomenclador().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).tipo(UPDATED_TIPO);
        // Add required entity
        Mutual mutual;
        if (TestUtil.findAll(em, Mutual.class).isEmpty()) {
            mutual = MutualResourceIT.createUpdatedEntity();
            em.persist(mutual);
            em.flush();
        } else {
            mutual = TestUtil.findAll(em, Mutual.class).get(0);
        }
        updatedNomenclador.setMutual(mutual);
        return updatedNomenclador;
    }

    @BeforeEach
    void initTest() {
        nomenclador = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedNomenclador != null) {
            nomencladorRepository.delete(insertedNomenclador);
            insertedNomenclador = null;
        }
    }

    @Test
    @Transactional
    void createNomenclador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);
        var returnedNomencladorDTO = om.readValue(
            restNomencladorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nomencladorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NomencladorDTO.class
        );

        // Validate the Nomenclador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNomenclador = nomencladorMapper.toEntity(returnedNomencladorDTO);
        assertNomencladorUpdatableFieldsEquals(returnedNomenclador, getPersistedNomenclador(returnedNomenclador));

        insertedNomenclador = returnedNomenclador;
    }

    @Test
    @Transactional
    void createNomencladorWithExistingId() throws Exception {
        // Create the Nomenclador with an existing ID
        nomenclador.setId(1L);
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNomencladorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nomencladorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nomenclador.setNombre(null);

        // Create the Nomenclador, which fails.
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        restNomencladorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nomencladorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nomenclador.setTipo(null);

        // Create the Nomenclador, which fails.
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        restNomencladorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nomencladorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNomencladors() throws Exception {
        // Initialize the database
        insertedNomenclador = nomencladorRepository.saveAndFlush(nomenclador);

        // Get all the nomencladorList
        restNomencladorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nomenclador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNomencladorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(nomencladorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNomencladorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(nomencladorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNomencladorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(nomencladorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNomencladorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(nomencladorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNomenclador() throws Exception {
        // Initialize the database
        insertedNomenclador = nomencladorRepository.saveAndFlush(nomenclador);

        // Get the nomenclador
        restNomencladorMockMvc
            .perform(get(ENTITY_API_URL_ID, nomenclador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nomenclador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNomenclador() throws Exception {
        // Get the nomenclador
        restNomencladorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNomenclador() throws Exception {
        // Initialize the database
        insertedNomenclador = nomencladorRepository.saveAndFlush(nomenclador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nomenclador
        Nomenclador updatedNomenclador = nomencladorRepository.findById(nomenclador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNomenclador are not directly saved in db
        em.detach(updatedNomenclador);
        updatedNomenclador.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).tipo(UPDATED_TIPO);
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(updatedNomenclador);

        restNomencladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nomencladorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nomencladorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNomencladorToMatchAllProperties(updatedNomenclador);
    }

    @Test
    @Transactional
    void putNonExistingNomenclador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nomenclador.setId(longCount.incrementAndGet());

        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNomencladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nomencladorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nomencladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNomenclador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nomenclador.setId(longCount.incrementAndGet());

        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomencladorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nomencladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNomenclador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nomenclador.setId(longCount.incrementAndGet());

        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomencladorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nomencladorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNomencladorWithPatch() throws Exception {
        // Initialize the database
        insertedNomenclador = nomencladorRepository.saveAndFlush(nomenclador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nomenclador using partial update
        Nomenclador partialUpdatedNomenclador = new Nomenclador();
        partialUpdatedNomenclador.setId(nomenclador.getId());

        restNomencladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNomenclador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNomenclador))
            )
            .andExpect(status().isOk());

        // Validate the Nomenclador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNomencladorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNomenclador, nomenclador),
            getPersistedNomenclador(nomenclador)
        );
    }

    @Test
    @Transactional
    void fullUpdateNomencladorWithPatch() throws Exception {
        // Initialize the database
        insertedNomenclador = nomencladorRepository.saveAndFlush(nomenclador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nomenclador using partial update
        Nomenclador partialUpdatedNomenclador = new Nomenclador();
        partialUpdatedNomenclador.setId(nomenclador.getId());

        partialUpdatedNomenclador.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION).tipo(UPDATED_TIPO);

        restNomencladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNomenclador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNomenclador))
            )
            .andExpect(status().isOk());

        // Validate the Nomenclador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNomencladorUpdatableFieldsEquals(partialUpdatedNomenclador, getPersistedNomenclador(partialUpdatedNomenclador));
    }

    @Test
    @Transactional
    void patchNonExistingNomenclador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nomenclador.setId(longCount.incrementAndGet());

        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNomencladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nomencladorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nomencladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNomenclador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nomenclador.setId(longCount.incrementAndGet());

        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomencladorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nomencladorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNomenclador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nomenclador.setId(longCount.incrementAndGet());

        // Create the Nomenclador
        NomencladorDTO nomencladorDTO = nomencladorMapper.toDto(nomenclador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomencladorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nomencladorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nomenclador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNomenclador() throws Exception {
        // Initialize the database
        insertedNomenclador = nomencladorRepository.saveAndFlush(nomenclador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nomenclador
        restNomencladorMockMvc
            .perform(delete(ENTITY_API_URL_ID, nomenclador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nomencladorRepository.count();
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

    protected Nomenclador getPersistedNomenclador(Nomenclador nomenclador) {
        return nomencladorRepository.findById(nomenclador.getId()).orElseThrow();
    }

    protected void assertPersistedNomencladorToMatchAllProperties(Nomenclador expectedNomenclador) {
        assertNomencladorAllPropertiesEquals(expectedNomenclador, getPersistedNomenclador(expectedNomenclador));
    }

    protected void assertPersistedNomencladorToMatchUpdatableProperties(Nomenclador expectedNomenclador) {
        assertNomencladorAllUpdatablePropertiesEquals(expectedNomenclador, getPersistedNomenclador(expectedNomenclador));
    }
}
