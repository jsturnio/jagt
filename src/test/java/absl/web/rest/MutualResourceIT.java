package absl.web.rest;

import static absl.domain.MutualAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Mutual;
import absl.domain.enumeration.TipoIva;
import absl.repository.MutualRepository;
import absl.service.dto.MutualDTO;
import absl.service.mapper.MutualMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link MutualResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MutualResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_CUIT = "AAAAAAAAAA";
    private static final String UPDATED_CUIT = "BBBBBBBBBB";

    private static final TipoIva DEFAULT_TIPO_IVA = TipoIva.RESP_NO_INSCRIPTO;
    private static final TipoIva UPDATED_TIPO_IVA = TipoIva.RESP_INSCRIPTO;

    private static final String DEFAULT_DOMICILIO = "AAAAAAAAAA";
    private static final String UPDATED_DOMICILIO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "994 302 \\dddd";
    private static final String UPDATED_TELEFONO = "437-069\\dddd";

    private static final String DEFAULT_EMAIL = ";@\\SSSSS";
    private static final String UPDATED_EMAIL = "BH@\\SS";

    private static final Boolean DEFAULT_HABILITADA = false;
    private static final Boolean UPDATED_HABILITADA = true;

    private static final String ENTITY_API_URL = "/api/mutuals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MutualRepository mutualRepository;

    @Autowired
    private MutualMapper mutualMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMutualMockMvc;

    private Mutual mutual;

    private Mutual insertedMutual;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mutual createEntity() {
        return new Mutual()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .cuit(DEFAULT_CUIT)
            .tipoIva(DEFAULT_TIPO_IVA)
            .domicilio(DEFAULT_DOMICILIO)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .habilitada(DEFAULT_HABILITADA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mutual createUpdatedEntity() {
        return new Mutual()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilio(UPDATED_DOMICILIO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .habilitada(UPDATED_HABILITADA);
    }

    @BeforeEach
    void initTest() {
        mutual = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMutual != null) {
            mutualRepository.delete(insertedMutual);
            insertedMutual = null;
        }
    }

    @Test
    @Transactional
    void createMutual() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);
        var returnedMutualDTO = om.readValue(
            restMutualMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MutualDTO.class
        );

        // Validate the Mutual in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMutual = mutualMapper.toEntity(returnedMutualDTO);
        assertMutualUpdatableFieldsEquals(returnedMutual, getPersistedMutual(returnedMutual));

        insertedMutual = returnedMutual;
    }

    @Test
    @Transactional
    void createMutualWithExistingId() throws Exception {
        // Create the Mutual with an existing ID
        mutual.setId(1L);
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mutual.setNombre(null);

        // Create the Mutual, which fails.
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        restMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mutual.setTipoIva(null);

        // Create the Mutual, which fails.
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        restMutualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMutuals() throws Exception {
        // Initialize the database
        insertedMutual = mutualRepository.saveAndFlush(mutual);

        // Get all the mutualList
        restMutualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mutual.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].cuit").value(hasItem(DEFAULT_CUIT)))
            .andExpect(jsonPath("$.[*].tipoIva").value(hasItem(DEFAULT_TIPO_IVA.toString())))
            .andExpect(jsonPath("$.[*].domicilio").value(hasItem(DEFAULT_DOMICILIO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].habilitada").value(hasItem(DEFAULT_HABILITADA)));
    }

    @Test
    @Transactional
    void getMutual() throws Exception {
        // Initialize the database
        insertedMutual = mutualRepository.saveAndFlush(mutual);

        // Get the mutual
        restMutualMockMvc
            .perform(get(ENTITY_API_URL_ID, mutual.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mutual.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.cuit").value(DEFAULT_CUIT))
            .andExpect(jsonPath("$.tipoIva").value(DEFAULT_TIPO_IVA.toString()))
            .andExpect(jsonPath("$.domicilio").value(DEFAULT_DOMICILIO))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.habilitada").value(DEFAULT_HABILITADA));
    }

    @Test
    @Transactional
    void getNonExistingMutual() throws Exception {
        // Get the mutual
        restMutualMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMutual() throws Exception {
        // Initialize the database
        insertedMutual = mutualRepository.saveAndFlush(mutual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mutual
        Mutual updatedMutual = mutualRepository.findById(mutual.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMutual are not directly saved in db
        em.detach(updatedMutual);
        updatedMutual
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilio(UPDATED_DOMICILIO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .habilitada(UPDATED_HABILITADA);
        MutualDTO mutualDTO = mutualMapper.toDto(updatedMutual);

        restMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mutualDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMutualToMatchAllProperties(updatedMutual);
    }

    @Test
    @Transactional
    void putNonExistingMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mutual.setId(longCount.incrementAndGet());

        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mutualDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mutual.setId(longCount.incrementAndGet());

        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mutual.setId(longCount.incrementAndGet());

        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mutualDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMutualWithPatch() throws Exception {
        // Initialize the database
        insertedMutual = mutualRepository.saveAndFlush(mutual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mutual using partial update
        Mutual partialUpdatedMutual = new Mutual();
        partialUpdatedMutual.setId(mutual.getId());

        partialUpdatedMutual
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .email(UPDATED_EMAIL);

        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMutual))
            )
            .andExpect(status().isOk());

        // Validate the Mutual in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMutualUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMutual, mutual), getPersistedMutual(mutual));
    }

    @Test
    @Transactional
    void fullUpdateMutualWithPatch() throws Exception {
        // Initialize the database
        insertedMutual = mutualRepository.saveAndFlush(mutual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mutual using partial update
        Mutual partialUpdatedMutual = new Mutual();
        partialUpdatedMutual.setId(mutual.getId());

        partialUpdatedMutual
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilio(UPDATED_DOMICILIO)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .habilitada(UPDATED_HABILITADA);

        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMutual.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMutual))
            )
            .andExpect(status().isOk());

        // Validate the Mutual in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMutualUpdatableFieldsEquals(partialUpdatedMutual, getPersistedMutual(partialUpdatedMutual));
    }

    @Test
    @Transactional
    void patchNonExistingMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mutual.setId(longCount.incrementAndGet());

        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mutualDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mutual.setId(longCount.incrementAndGet());

        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mutualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMutual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mutual.setId(longCount.incrementAndGet());

        // Create the Mutual
        MutualDTO mutualDTO = mutualMapper.toDto(mutual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMutualMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mutualDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mutual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMutual() throws Exception {
        // Initialize the database
        insertedMutual = mutualRepository.saveAndFlush(mutual);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mutual
        restMutualMockMvc
            .perform(delete(ENTITY_API_URL_ID, mutual.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mutualRepository.count();
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

    protected Mutual getPersistedMutual(Mutual mutual) {
        return mutualRepository.findById(mutual.getId()).orElseThrow();
    }

    protected void assertPersistedMutualToMatchAllProperties(Mutual expectedMutual) {
        assertMutualAllPropertiesEquals(expectedMutual, getPersistedMutual(expectedMutual));
    }

    protected void assertPersistedMutualToMatchUpdatableProperties(Mutual expectedMutual) {
        assertMutualAllUpdatablePropertiesEquals(expectedMutual, getPersistedMutual(expectedMutual));
    }
}
