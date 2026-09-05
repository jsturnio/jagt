package absl.web.rest;

import static absl.domain.OrdenAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static absl.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Bioquimico;
import absl.domain.Empleado;
import absl.domain.Orden;
import absl.domain.Paquete;
import absl.repository.OrdenRepository;
import absl.service.OrdenService;
import absl.service.dto.OrdenDTO;
import absl.service.mapper.OrdenMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link OrdenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrdenResourceIT {

    private static final Long DEFAULT_AFILIADO = 1L;
    private static final Long UPDATED_AFILIADO = 2L;

    private static final Long DEFAULT_PROTOCOLO = 1L;
    private static final Long UPDATED_PROTOCOLO = 2L;

    private static final LocalDate DEFAULT_FECHA_ORDEN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ORDEN = LocalDate.parse("2023-12-11");

    private static final LocalDate DEFAULT_FECHA_PRESCRIPCION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PRESCRIPCION = LocalDate.parse("2023-12-11");

    private static final BigDecimal DEFAULT_TOTAL_ORDEN = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_ORDEN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUMA_UB = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUMA_UB = new BigDecimal(2);

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.ofEpochMilli(1702252943173L);

    private static final String ENTITY_API_URL = "/api/ordens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrdenRepository ordenRepository;

    @Mock
    private OrdenRepository ordenRepositoryMock;

    @Autowired
    private OrdenMapper ordenMapper;

    @Mock
    private OrdenService ordenServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdenMockMvc;

    private Orden orden;

    private Orden insertedOrden;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orden createEntity(EntityManager em) {
        Orden orden = new Orden()
            .afiliado(DEFAULT_AFILIADO)
            .protocolo(DEFAULT_PROTOCOLO)
            .fechaOrden(DEFAULT_FECHA_ORDEN)
            .fechaPrescripcion(DEFAULT_FECHA_PRESCRIPCION)
            .totalOrden(DEFAULT_TOTAL_ORDEN)
            .sumaUb(DEFAULT_SUMA_UB)
            .fechaCreacion(DEFAULT_FECHA_CREACION);
        // Add required entity
        Paquete paquete;
        if (TestUtil.findAll(em, Paquete.class).isEmpty()) {
            paquete = PaqueteResourceIT.createEntity(em);
            em.persist(paquete);
            em.flush();
        } else {
            paquete = TestUtil.findAll(em, Paquete.class).get(0);
        }
        orden.setPaquete(paquete);
        // Add required entity
        Empleado empleado;
        if (TestUtil.findAll(em, Empleado.class).isEmpty()) {
            empleado = EmpleadoResourceIT.createEntity(em);
            em.persist(empleado);
            em.flush();
        } else {
            empleado = TestUtil.findAll(em, Empleado.class).get(0);
        }
        orden.setUsuario(empleado);
        // Add required entity
        Bioquimico bioquimico;
        if (TestUtil.findAll(em, Bioquimico.class).isEmpty()) {
            bioquimico = BioquimicoResourceIT.createEntity(em);
            em.persist(bioquimico);
            em.flush();
        } else {
            bioquimico = TestUtil.findAll(em, Bioquimico.class).get(0);
        }
        orden.setBioquimico(bioquimico);
        return orden;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orden createUpdatedEntity(EntityManager em) {
        Orden updatedOrden = new Orden()
            .afiliado(UPDATED_AFILIADO)
            .protocolo(UPDATED_PROTOCOLO)
            .fechaOrden(UPDATED_FECHA_ORDEN)
            .fechaPrescripcion(UPDATED_FECHA_PRESCRIPCION)
            .totalOrden(UPDATED_TOTAL_ORDEN)
            .sumaUb(UPDATED_SUMA_UB)
            .fechaCreacion(UPDATED_FECHA_CREACION);
        // Add required entity
        Paquete paquete;
        if (TestUtil.findAll(em, Paquete.class).isEmpty()) {
            paquete = PaqueteResourceIT.createUpdatedEntity(em);
            em.persist(paquete);
            em.flush();
        } else {
            paquete = TestUtil.findAll(em, Paquete.class).get(0);
        }
        updatedOrden.setPaquete(paquete);
        // Add required entity
        Empleado empleado;
        if (TestUtil.findAll(em, Empleado.class).isEmpty()) {
            empleado = EmpleadoResourceIT.createUpdatedEntity(em);
            em.persist(empleado);
            em.flush();
        } else {
            empleado = TestUtil.findAll(em, Empleado.class).get(0);
        }
        updatedOrden.setUsuario(empleado);
        // Add required entity
        Bioquimico bioquimico;
        if (TestUtil.findAll(em, Bioquimico.class).isEmpty()) {
            bioquimico = BioquimicoResourceIT.createUpdatedEntity(em);
            em.persist(bioquimico);
            em.flush();
        } else {
            bioquimico = TestUtil.findAll(em, Bioquimico.class).get(0);
        }
        updatedOrden.setBioquimico(bioquimico);
        return updatedOrden;
    }

    @BeforeEach
    void initTest() {
        orden = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOrden != null) {
            ordenRepository.delete(insertedOrden);
            insertedOrden = null;
        }
    }

    @Test
    @Transactional
    void createOrden() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);
        var returnedOrdenDTO = om.readValue(
            restOrdenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ordenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrdenDTO.class
        );

        // Validate the Orden in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrden = ordenMapper.toEntity(returnedOrdenDTO);
        assertOrdenUpdatableFieldsEquals(returnedOrden, getPersistedOrden(returnedOrden));

        insertedOrden = returnedOrden;
    }

    @Test
    @Transactional
    void createOrdenWithExistingId() throws Exception {
        // Create the Orden with an existing ID
        orden.setId(1L);
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ordenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdens() throws Exception {
        // Initialize the database
        insertedOrden = ordenRepository.saveAndFlush(orden);

        // Get all the ordenList
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orden.getId().intValue())))
            .andExpect(jsonPath("$.[*].afiliado").value(hasItem(DEFAULT_AFILIADO.intValue())))
            .andExpect(jsonPath("$.[*].protocolo").value(hasItem(DEFAULT_PROTOCOLO.intValue())))
            .andExpect(jsonPath("$.[*].fechaOrden").value(hasItem(DEFAULT_FECHA_ORDEN.toString())))
            .andExpect(jsonPath("$.[*].fechaPrescripcion").value(hasItem(DEFAULT_FECHA_PRESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].totalOrden").value(hasItem(sameNumber(DEFAULT_TOTAL_ORDEN))))
            .andExpect(jsonPath("$.[*].sumaUb").value(hasItem(sameNumber(DEFAULT_SUMA_UB))))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdensWithEagerRelationshipsIsEnabled() throws Exception {
        when(ordenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ordenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ordenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ordenRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOrden() throws Exception {
        // Initialize the database
        insertedOrden = ordenRepository.saveAndFlush(orden);

        // Get the orden
        restOrdenMockMvc
            .perform(get(ENTITY_API_URL_ID, orden.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orden.getId().intValue()))
            .andExpect(jsonPath("$.afiliado").value(DEFAULT_AFILIADO.intValue()))
            .andExpect(jsonPath("$.protocolo").value(DEFAULT_PROTOCOLO.intValue()))
            .andExpect(jsonPath("$.fechaOrden").value(DEFAULT_FECHA_ORDEN.toString()))
            .andExpect(jsonPath("$.fechaPrescripcion").value(DEFAULT_FECHA_PRESCRIPCION.toString()))
            .andExpect(jsonPath("$.totalOrden").value(sameNumber(DEFAULT_TOTAL_ORDEN)))
            .andExpect(jsonPath("$.sumaUb").value(sameNumber(DEFAULT_SUMA_UB)))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrden() throws Exception {
        // Get the orden
        restOrdenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrden() throws Exception {
        // Initialize the database
        insertedOrden = ordenRepository.saveAndFlush(orden);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orden
        Orden updatedOrden = ordenRepository.findById(orden.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrden are not directly saved in db
        em.detach(updatedOrden);
        updatedOrden
            .afiliado(UPDATED_AFILIADO)
            .protocolo(UPDATED_PROTOCOLO)
            .fechaOrden(UPDATED_FECHA_ORDEN)
            .fechaPrescripcion(UPDATED_FECHA_PRESCRIPCION)
            .totalOrden(UPDATED_TOTAL_ORDEN)
            .sumaUb(UPDATED_SUMA_UB)
            .fechaCreacion(UPDATED_FECHA_CREACION);
        OrdenDTO ordenDTO = ordenMapper.toDto(updatedOrden);

        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ordenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrdenToMatchAllProperties(updatedOrden);
    }

    @Test
    @Transactional
    void putNonExistingOrden() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orden.setId(longCount.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrden() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orden.setId(longCount.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrden() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orden.setId(longCount.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ordenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdenWithPatch() throws Exception {
        // Initialize the database
        insertedOrden = ordenRepository.saveAndFlush(orden);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orden using partial update
        Orden partialUpdatedOrden = new Orden();
        partialUpdatedOrden.setId(orden.getId());

        partialUpdatedOrden.afiliado(UPDATED_AFILIADO).fechaOrden(UPDATED_FECHA_ORDEN).totalOrden(UPDATED_TOTAL_ORDEN);

        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrdenUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOrden, orden), getPersistedOrden(orden));
    }

    @Test
    @Transactional
    void fullUpdateOrdenWithPatch() throws Exception {
        // Initialize the database
        insertedOrden = ordenRepository.saveAndFlush(orden);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orden using partial update
        Orden partialUpdatedOrden = new Orden();
        partialUpdatedOrden.setId(orden.getId());

        partialUpdatedOrden
            .afiliado(UPDATED_AFILIADO)
            .protocolo(UPDATED_PROTOCOLO)
            .fechaOrden(UPDATED_FECHA_ORDEN)
            .fechaPrescripcion(UPDATED_FECHA_PRESCRIPCION)
            .totalOrden(UPDATED_TOTAL_ORDEN)
            .sumaUb(UPDATED_SUMA_UB)
            .fechaCreacion(UPDATED_FECHA_CREACION);

        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrden.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrden))
            )
            .andExpect(status().isOk());

        // Validate the Orden in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrdenUpdatableFieldsEquals(partialUpdatedOrden, getPersistedOrden(partialUpdatedOrden));
    }

    @Test
    @Transactional
    void patchNonExistingOrden() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orden.setId(longCount.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrden() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orden.setId(longCount.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ordenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrden() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orden.setId(longCount.incrementAndGet());

        // Create the Orden
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ordenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orden in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrden() throws Exception {
        // Initialize the database
        insertedOrden = ordenRepository.saveAndFlush(orden);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orden
        restOrdenMockMvc
            .perform(delete(ENTITY_API_URL_ID, orden.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ordenRepository.count();
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

    protected Orden getPersistedOrden(Orden orden) {
        return ordenRepository.findById(orden.getId()).orElseThrow();
    }

    protected void assertPersistedOrdenToMatchAllProperties(Orden expectedOrden) {
        assertOrdenAllPropertiesEquals(expectedOrden, getPersistedOrden(expectedOrden));
    }

    protected void assertPersistedOrdenToMatchUpdatableProperties(Orden expectedOrden) {
        assertOrdenAllUpdatablePropertiesEquals(expectedOrden, getPersistedOrden(expectedOrden));
    }
}
