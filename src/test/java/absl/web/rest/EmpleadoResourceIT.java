package absl.web.rest;

import static absl.domain.EmpleadoAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Empleado;
import absl.domain.User;
import absl.domain.enumeration.Gender;
import absl.domain.enumeration.TipoIva;
import absl.repository.EmpleadoRepository;
import absl.repository.UserRepository;
import absl.service.EmpleadoService;
import absl.service.dto.EmpleadoDTO;
import absl.service.mapper.EmpleadoMapper;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link EmpleadoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmpleadoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_CUIT = "AAAAAAAAAA";
    private static final String UPDATED_CUIT = "BBBBBBBBBB";

    private static final TipoIva DEFAULT_TIPO_IVA = TipoIva.RESP_NO_INSCRIPTO;
    private static final TipoIva UPDATED_TIPO_IVA = TipoIva.RESP_INSCRIPTO;

    private static final String DEFAULT_DOMICILIO = "AAAAAAAAAA";
    private static final String UPDATED_DOMICILIO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "?<iy@v";
    private static final String UPDATED_EMAIL = "f@U)4=@NOG`T";

    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.parse("2023-12-11");

    private static final String DEFAULT_TELEFONO = "073062.883";
    private static final String UPDATED_TELEFONO = "636-755-889";

    private static final Gender DEFAULT_GENERO = Gender.HOMBRE;
    private static final Gender UPDATED_GENERO = Gender.MUJER;

    private static final String ENTITY_API_URL = "/api/empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EmpleadoRepository empleadoRepositoryMock;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Mock
    private EmpleadoService empleadoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    private Empleado insertedEmpleado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createEntity(EntityManager em) {
        Empleado empleado = new Empleado()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .cuit(DEFAULT_CUIT)
            .tipoIva(DEFAULT_TIPO_IVA)
            .domicilio(DEFAULT_DOMICILIO)
            .email(DEFAULT_EMAIL)
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .telefono(DEFAULT_TELEFONO)
            .genero(DEFAULT_GENERO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        empleado.setUsuario(user);
        return empleado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleado createUpdatedEntity(EntityManager em) {
        Empleado updatedEmpleado = new Empleado()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilio(UPDATED_DOMICILIO)
            .email(UPDATED_EMAIL)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .telefono(UPDATED_TELEFONO)
            .genero(UPDATED_GENERO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedEmpleado.setUsuario(user);
        return updatedEmpleado;
    }

    @BeforeEach
    void initTest() {
        empleado = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEmpleado != null) {
            empleadoRepository.delete(insertedEmpleado);
            insertedEmpleado = null;
        }
    }

    @Test
    @Transactional
    void createEmpleado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);
        var returnedEmpleadoDTO = om.readValue(
            restEmpleadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpleadoDTO.class
        );

        // Validate the Empleado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmpleado = empleadoMapper.toEntity(returnedEmpleadoDTO);
        assertEmpleadoUpdatableFieldsEquals(returnedEmpleado, getPersistedEmpleado(returnedEmpleado));

        insertedEmpleado = returnedEmpleado;
    }

    @Test
    @Transactional
    void createEmpleadoWithExistingId() throws Exception {
        // Create the Empleado with an existing ID
        empleado.setId(1L);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setTipoIva(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setEmail(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empleado.setGenero(null);

        // Create the Empleado, which fails.
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        restEmpleadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmpleados() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get all the empleadoList
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].cuit").value(hasItem(DEFAULT_CUIT)))
            .andExpect(jsonPath("$.[*].tipoIva").value(hasItem(DEFAULT_TIPO_IVA.toString())))
            .andExpect(jsonPath("$.[*].domicilio").value(hasItem(DEFAULT_DOMICILIO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpleadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(empleadoServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restEmpleadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empleadoServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpleadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(empleadoServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restEmpleadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(empleadoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc
            .perform(get(ENTITY_API_URL_ID, empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.cuit").value(DEFAULT_CUIT))
            .andExpect(jsonPath("$.tipoIva").value(DEFAULT_TIPO_IVA.toString()))
            .andExpect(jsonPath("$.domicilio").value(DEFAULT_DOMICILIO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado
        Empleado updatedEmpleado = empleadoRepository.findById(empleado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmpleado are not directly saved in db
        em.detach(updatedEmpleado);
        updatedEmpleado
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilio(UPDATED_DOMICILIO)
            .email(UPDATED_EMAIL)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .telefono(UPDATED_TELEFONO)
            .genero(UPDATED_GENERO);
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(updatedEmpleado);

        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadoToMatchAllProperties(updatedEmpleado);
    }

    @Test
    @Transactional
    void putNonExistingEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado.tipoIva(UPDATED_TIPO_IVA).genero(UPDATED_GENERO);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmpleado, empleado), getPersistedEmpleado(empleado));
    }

    @Test
    @Transactional
    void fullUpdateEmpleadoWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleado using partial update
        Empleado partialUpdatedEmpleado = new Empleado();
        partialUpdatedEmpleado.setId(empleado.getId());

        partialUpdatedEmpleado
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilio(UPDATED_DOMICILIO)
            .email(UPDATED_EMAIL)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .telefono(UPDATED_TELEFONO)
            .genero(UPDATED_GENERO);

        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpleado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpleado))
            )
            .andExpect(status().isOk());

        // Validate the Empleado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadoUpdatableFieldsEquals(partialUpdatedEmpleado, getPersistedEmpleado(partialUpdatedEmpleado));
    }

    @Test
    @Transactional
    void patchNonExistingEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empleadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empleadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpleado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleado.setId(longCount.incrementAndGet());

        // Create the Empleado
        EmpleadoDTO empleadoDTO = empleadoMapper.toDto(empleado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpleadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empleadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empleado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpleado() throws Exception {
        // Initialize the database
        insertedEmpleado = empleadoRepository.saveAndFlush(empleado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleado
        restEmpleadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, empleado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadoRepository.count();
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

    protected Empleado getPersistedEmpleado(Empleado empleado) {
        return empleadoRepository.findById(empleado.getId()).orElseThrow();
    }

    protected void assertPersistedEmpleadoToMatchAllProperties(Empleado expectedEmpleado) {
        assertEmpleadoAllPropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
    }

    protected void assertPersistedEmpleadoToMatchUpdatableProperties(Empleado expectedEmpleado) {
        assertEmpleadoAllUpdatablePropertiesEquals(expectedEmpleado, getPersistedEmpleado(expectedEmpleado));
    }
}
