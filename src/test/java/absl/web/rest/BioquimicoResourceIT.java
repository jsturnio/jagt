package absl.web.rest;

import static absl.domain.BioquimicoAsserts.*;
import static absl.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import absl.IntegrationTest;
import absl.domain.Bioquimico;
import absl.domain.User;
import absl.domain.enumeration.EstadoSocio;
import absl.domain.enumeration.Gender;
import absl.domain.enumeration.TipoIva;
import absl.repository.BioquimicoRepository;
import absl.repository.UserRepository;
import absl.service.BioquimicoService;
import absl.service.dto.BioquimicoDTO;
import absl.service.mapper.BioquimicoMapper;
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
 * Integration tests for the {@link BioquimicoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BioquimicoResourceIT {

    private static final Long DEFAULT_MATRICULA = 1L;
    private static final Long UPDATED_MATRICULA = 2L;

    private static final String DEFAULT_CUIT = "AAAAAAAAAA";
    private static final String UPDATED_CUIT = "BBBBBBBBBB";

    private static final TipoIva DEFAULT_TIPO_IVA = TipoIva.RESP_NO_INSCRIPTO;
    private static final TipoIva UPDATED_TIPO_IVA = TipoIva.RESP_INSCRIPTO;

    private static final String DEFAULT_DOMICILIO_PROFESIONAL = "AAAAAAAAAA";
    private static final String UPDATED_DOMICILIO_PROFESIONAL = "BBBBBBBBBB";

    private static final EstadoSocio DEFAULT_ESTADO = EstadoSocio.SOCIO_ACTIVO;
    private static final EstadoSocio UPDATED_ESTADO = EstadoSocio.NO_CARGA;

    private static final String DEFAULT_EMAIL = "x>l@3#/";
    private static final String UPDATED_EMAIL = "6``)7q@.Onx0O";

    private static final LocalDate DEFAULT_FECHA_INGRESO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INGRESO = LocalDate.parse("2023-12-11");

    private static final String DEFAULT_TELEFONO = "101151317";
    private static final String UPDATED_TELEFONO = "419.278-821";

    private static final String DEFAULT_NOMBRE_EN_DOSEP = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_EN_DOSEP = "BBBBBBBBBB";

    private static final Integer DEFAULT_NRO_PRESTADOR_OSDE = 1;
    private static final Integer UPDATED_NRO_PRESTADOR_OSDE = 2;

    private static final String DEFAULT_ING_BRUTOS = "AAAAAAAAAA";
    private static final String UPDATED_ING_BRUTOS = "BBBBBBBBBB";

    private static final String DEFAULT_NRO_JUBILACION = "AAAAAAAAAA";
    private static final String UPDATED_NRO_JUBILACION = "BBBBBBBBBB";

    private static final String DEFAULT_NRO_LABORATORIO = "AAAAAAAAAA";
    private static final String UPDATED_NRO_LABORATORIO = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENERO = Gender.HOMBRE;
    private static final Gender UPDATED_GENERO = Gender.MUJER;

    private static final String ENTITY_API_URL = "/api/bioquimicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BioquimicoRepository bioquimicoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BioquimicoRepository bioquimicoRepositoryMock;

    @Autowired
    private BioquimicoMapper bioquimicoMapper;

    @Mock
    private BioquimicoService bioquimicoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBioquimicoMockMvc;

    private Bioquimico bioquimico;

    private Bioquimico insertedBioquimico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bioquimico createEntity(EntityManager em) {
        Bioquimico bioquimico = new Bioquimico()
            .matricula(DEFAULT_MATRICULA)
            .cuit(DEFAULT_CUIT)
            .tipoIva(DEFAULT_TIPO_IVA)
            .domicilioProfesional(DEFAULT_DOMICILIO_PROFESIONAL)
            .estado(DEFAULT_ESTADO)
            .email(DEFAULT_EMAIL)
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .telefono(DEFAULT_TELEFONO)
            .nombreEnDosep(DEFAULT_NOMBRE_EN_DOSEP)
            .nroPrestadorOsde(DEFAULT_NRO_PRESTADOR_OSDE)
            .ingBrutos(DEFAULT_ING_BRUTOS)
            .nroJubilacion(DEFAULT_NRO_JUBILACION)
            .nroLaboratorio(DEFAULT_NRO_LABORATORIO)
            .genero(DEFAULT_GENERO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        bioquimico.setUser(user);
        return bioquimico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bioquimico createUpdatedEntity(EntityManager em) {
        Bioquimico updatedBioquimico = new Bioquimico()
            .matricula(UPDATED_MATRICULA)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilioProfesional(UPDATED_DOMICILIO_PROFESIONAL)
            .estado(UPDATED_ESTADO)
            .email(UPDATED_EMAIL)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .telefono(UPDATED_TELEFONO)
            .nombreEnDosep(UPDATED_NOMBRE_EN_DOSEP)
            .nroPrestadorOsde(UPDATED_NRO_PRESTADOR_OSDE)
            .ingBrutos(UPDATED_ING_BRUTOS)
            .nroJubilacion(UPDATED_NRO_JUBILACION)
            .nroLaboratorio(UPDATED_NRO_LABORATORIO)
            .genero(UPDATED_GENERO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedBioquimico.setUser(user);
        return updatedBioquimico;
    }

    @BeforeEach
    void initTest() {
        bioquimico = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBioquimico != null) {
            bioquimicoRepository.delete(insertedBioquimico);
            insertedBioquimico = null;
        }
    }

    @Test
    @Transactional
    void createBioquimico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);
        var returnedBioquimicoDTO = om.readValue(
            restBioquimicoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bioquimicoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BioquimicoDTO.class
        );

        // Validate the Bioquimico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBioquimico = bioquimicoMapper.toEntity(returnedBioquimicoDTO);
        assertBioquimicoUpdatableFieldsEquals(returnedBioquimico, getPersistedBioquimico(returnedBioquimico));

        insertedBioquimico = returnedBioquimico;
    }

    @Test
    @Transactional
    void createBioquimicoWithExistingId() throws Exception {
        // Create the Bioquimico with an existing ID
        bioquimico.setId(1L);
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBioquimicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bioquimicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bioquimico.setTipoIva(null);

        // Create the Bioquimico, which fails.
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        restBioquimicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bioquimicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bioquimico.setEmail(null);

        // Create the Bioquimico, which fails.
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        restBioquimicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bioquimicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bioquimico.setGenero(null);

        // Create the Bioquimico, which fails.
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        restBioquimicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bioquimicoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBioquimicos() throws Exception {
        // Initialize the database
        insertedBioquimico = bioquimicoRepository.saveAndFlush(bioquimico);

        // Get all the bioquimicoList
        restBioquimicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bioquimico.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA.intValue())))
            .andExpect(jsonPath("$.[*].cuit").value(hasItem(DEFAULT_CUIT)))
            .andExpect(jsonPath("$.[*].tipoIva").value(hasItem(DEFAULT_TIPO_IVA.toString())))
            .andExpect(jsonPath("$.[*].domicilioProfesional").value(hasItem(DEFAULT_DOMICILIO_PROFESIONAL)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].nombreEnDosep").value(hasItem(DEFAULT_NOMBRE_EN_DOSEP)))
            .andExpect(jsonPath("$.[*].nroPrestadorOsde").value(hasItem(DEFAULT_NRO_PRESTADOR_OSDE)))
            .andExpect(jsonPath("$.[*].ingBrutos").value(hasItem(DEFAULT_ING_BRUTOS)))
            .andExpect(jsonPath("$.[*].nroJubilacion").value(hasItem(DEFAULT_NRO_JUBILACION)))
            .andExpect(jsonPath("$.[*].nroLaboratorio").value(hasItem(DEFAULT_NRO_LABORATORIO)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBioquimicosWithEagerRelationshipsIsEnabled() throws Exception {
        when(bioquimicoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBioquimicoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bioquimicoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBioquimicosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bioquimicoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBioquimicoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bioquimicoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBioquimico() throws Exception {
        // Initialize the database
        insertedBioquimico = bioquimicoRepository.saveAndFlush(bioquimico);

        // Get the bioquimico
        restBioquimicoMockMvc
            .perform(get(ENTITY_API_URL_ID, bioquimico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bioquimico.getId().intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA.intValue()))
            .andExpect(jsonPath("$.cuit").value(DEFAULT_CUIT))
            .andExpect(jsonPath("$.tipoIva").value(DEFAULT_TIPO_IVA.toString()))
            .andExpect(jsonPath("$.domicilioProfesional").value(DEFAULT_DOMICILIO_PROFESIONAL))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.nombreEnDosep").value(DEFAULT_NOMBRE_EN_DOSEP))
            .andExpect(jsonPath("$.nroPrestadorOsde").value(DEFAULT_NRO_PRESTADOR_OSDE))
            .andExpect(jsonPath("$.ingBrutos").value(DEFAULT_ING_BRUTOS))
            .andExpect(jsonPath("$.nroJubilacion").value(DEFAULT_NRO_JUBILACION))
            .andExpect(jsonPath("$.nroLaboratorio").value(DEFAULT_NRO_LABORATORIO))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBioquimico() throws Exception {
        // Get the bioquimico
        restBioquimicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBioquimico() throws Exception {
        // Initialize the database
        insertedBioquimico = bioquimicoRepository.saveAndFlush(bioquimico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bioquimico
        Bioquimico updatedBioquimico = bioquimicoRepository.findById(bioquimico.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBioquimico are not directly saved in db
        em.detach(updatedBioquimico);
        updatedBioquimico
            .matricula(UPDATED_MATRICULA)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilioProfesional(UPDATED_DOMICILIO_PROFESIONAL)
            .estado(UPDATED_ESTADO)
            .email(UPDATED_EMAIL)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .telefono(UPDATED_TELEFONO)
            .nombreEnDosep(UPDATED_NOMBRE_EN_DOSEP)
            .nroPrestadorOsde(UPDATED_NRO_PRESTADOR_OSDE)
            .ingBrutos(UPDATED_ING_BRUTOS)
            .nroJubilacion(UPDATED_NRO_JUBILACION)
            .nroLaboratorio(UPDATED_NRO_LABORATORIO)
            .genero(UPDATED_GENERO);
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(updatedBioquimico);

        restBioquimicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bioquimicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bioquimicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBioquimicoToMatchAllProperties(updatedBioquimico);
    }

    @Test
    @Transactional
    void putNonExistingBioquimico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bioquimico.setId(longCount.incrementAndGet());

        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBioquimicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bioquimicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bioquimicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBioquimico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bioquimico.setId(longCount.incrementAndGet());

        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBioquimicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bioquimicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBioquimico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bioquimico.setId(longCount.incrementAndGet());

        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBioquimicoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bioquimicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBioquimicoWithPatch() throws Exception {
        // Initialize the database
        insertedBioquimico = bioquimicoRepository.saveAndFlush(bioquimico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bioquimico using partial update
        Bioquimico partialUpdatedBioquimico = new Bioquimico();
        partialUpdatedBioquimico.setId(bioquimico.getId());

        partialUpdatedBioquimico
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilioProfesional(UPDATED_DOMICILIO_PROFESIONAL)
            .estado(UPDATED_ESTADO)
            .email(UPDATED_EMAIL)
            .nroJubilacion(UPDATED_NRO_JUBILACION);

        restBioquimicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBioquimico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBioquimico))
            )
            .andExpect(status().isOk());

        // Validate the Bioquimico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBioquimicoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBioquimico, bioquimico),
            getPersistedBioquimico(bioquimico)
        );
    }

    @Test
    @Transactional
    void fullUpdateBioquimicoWithPatch() throws Exception {
        // Initialize the database
        insertedBioquimico = bioquimicoRepository.saveAndFlush(bioquimico);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bioquimico using partial update
        Bioquimico partialUpdatedBioquimico = new Bioquimico();
        partialUpdatedBioquimico.setId(bioquimico.getId());

        partialUpdatedBioquimico
            .matricula(UPDATED_MATRICULA)
            .cuit(UPDATED_CUIT)
            .tipoIva(UPDATED_TIPO_IVA)
            .domicilioProfesional(UPDATED_DOMICILIO_PROFESIONAL)
            .estado(UPDATED_ESTADO)
            .email(UPDATED_EMAIL)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .telefono(UPDATED_TELEFONO)
            .nombreEnDosep(UPDATED_NOMBRE_EN_DOSEP)
            .nroPrestadorOsde(UPDATED_NRO_PRESTADOR_OSDE)
            .ingBrutos(UPDATED_ING_BRUTOS)
            .nroJubilacion(UPDATED_NRO_JUBILACION)
            .nroLaboratorio(UPDATED_NRO_LABORATORIO)
            .genero(UPDATED_GENERO);

        restBioquimicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBioquimico.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBioquimico))
            )
            .andExpect(status().isOk());

        // Validate the Bioquimico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBioquimicoUpdatableFieldsEquals(partialUpdatedBioquimico, getPersistedBioquimico(partialUpdatedBioquimico));
    }

    @Test
    @Transactional
    void patchNonExistingBioquimico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bioquimico.setId(longCount.incrementAndGet());

        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBioquimicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bioquimicoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bioquimicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBioquimico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bioquimico.setId(longCount.incrementAndGet());

        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBioquimicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bioquimicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBioquimico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bioquimico.setId(longCount.incrementAndGet());

        // Create the Bioquimico
        BioquimicoDTO bioquimicoDTO = bioquimicoMapper.toDto(bioquimico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBioquimicoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bioquimicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bioquimico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBioquimico() throws Exception {
        // Initialize the database
        insertedBioquimico = bioquimicoRepository.saveAndFlush(bioquimico);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bioquimico
        restBioquimicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, bioquimico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bioquimicoRepository.count();
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

    protected Bioquimico getPersistedBioquimico(Bioquimico bioquimico) {
        return bioquimicoRepository.findById(bioquimico.getId()).orElseThrow();
    }

    protected void assertPersistedBioquimicoToMatchAllProperties(Bioquimico expectedBioquimico) {
        assertBioquimicoAllPropertiesEquals(expectedBioquimico, getPersistedBioquimico(expectedBioquimico));
    }

    protected void assertPersistedBioquimicoToMatchUpdatableProperties(Bioquimico expectedBioquimico) {
        assertBioquimicoAllUpdatablePropertiesEquals(expectedBioquimico, getPersistedBioquimico(expectedBioquimico));
    }
}
