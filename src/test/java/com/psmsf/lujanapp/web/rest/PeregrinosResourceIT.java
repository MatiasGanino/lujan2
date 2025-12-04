package com.psmsf.lujanapp.web.rest;

import static com.psmsf.lujanapp.domain.PeregrinosAsserts.*;
import static com.psmsf.lujanapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psmsf.lujanapp.IntegrationTest;
import com.psmsf.lujanapp.domain.Peregrinos;
import com.psmsf.lujanapp.domain.enumeration.TipoFormaPago;
import com.psmsf.lujanapp.domain.enumeration.TipoPersona;
import com.psmsf.lujanapp.domain.enumeration.TipoSalida;
import com.psmsf.lujanapp.repository.PeregrinosRepository;
import com.psmsf.lujanapp.service.dto.PeregrinosDTO;
import com.psmsf.lujanapp.service.mapper.PeregrinosMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeregrinosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeregrinosResourceIT {

    private static final Integer DEFAULT_NUMERO_ESPECIAL = 1;
    private static final Integer UPDATED_NUMERO_ESPECIAL = 2;
    private static final Integer SMALLER_NUMERO_ESPECIAL = 1 - 1;

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final TipoPersona DEFAULT_MAYOR_MENOR = TipoPersona.MAYOR;
    private static final TipoPersona UPDATED_MAYOR_MENOR = TipoPersona.MENOR;

    private static final TipoSalida DEFAULT_SALIDA = TipoSalida.LINIERS;
    private static final TipoSalida UPDATED_SALIDA = TipoSalida.RODRIGUEZ;

    private static final Integer DEFAULT_P_AGO = 1;
    private static final Integer UPDATED_P_AGO = 2;
    private static final Integer SMALLER_P_AGO = 1 - 1;

    private static final TipoFormaPago DEFAULT_FORMA_PAGO = TipoFormaPago.EFECTIVO;
    private static final TipoFormaPago UPDATED_FORMA_PAGO = TipoFormaPago.TRANSFERENCIA;

    private static final String DEFAULT_ACLARACIONES = "AAAAAAAAAA";
    private static final String UPDATED_ACLARACIONES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETO_FORMULARIO = false;
    private static final Boolean UPDATED_COMPLETO_FORMULARIO = true;

    private static final String ENTITY_API_URL = "/api/peregrinos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PeregrinosRepository peregrinosRepository;

    @Autowired
    private PeregrinosMapper peregrinosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeregrinosMockMvc;

    private Peregrinos peregrinos;

    private Peregrinos insertedPeregrinos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peregrinos createEntity() {
        return new Peregrinos()
            .numeroEspecial(DEFAULT_NUMERO_ESPECIAL)
            .apellido(DEFAULT_APELLIDO)
            .nombre(DEFAULT_NOMBRE)
            .numeroDocumento(DEFAULT_NUMERO_DOCUMENTO)
            .telefono(DEFAULT_TELEFONO)
            .mayorMenor(DEFAULT_MAYOR_MENOR)
            .salida(DEFAULT_SALIDA)
            .pago(DEFAULT_P_AGO)
            .formaPago(DEFAULT_FORMA_PAGO)
            .aclaraciones(DEFAULT_ACLARACIONES)
            .completoFormulario(DEFAULT_COMPLETO_FORMULARIO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peregrinos createUpdatedEntity() {
        return new Peregrinos()
            .numeroEspecial(UPDATED_NUMERO_ESPECIAL)
            .apellido(UPDATED_APELLIDO)
            .nombre(UPDATED_NOMBRE)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .telefono(UPDATED_TELEFONO)
            .mayorMenor(UPDATED_MAYOR_MENOR)
            .salida(UPDATED_SALIDA)
            .pago(UPDATED_P_AGO)
            .formaPago(UPDATED_FORMA_PAGO)
            .aclaraciones(UPDATED_ACLARACIONES)
            .completoFormulario(UPDATED_COMPLETO_FORMULARIO);
    }

    @BeforeEach
    void initTest() {
        peregrinos = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPeregrinos != null) {
            peregrinosRepository.delete(insertedPeregrinos);
            insertedPeregrinos = null;
        }
    }

    @Test
    @Transactional
    void createPeregrinos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);
        var returnedPeregrinosDTO = om.readValue(
            restPeregrinosMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PeregrinosDTO.class
        );

        // Validate the Peregrinos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPeregrinos = peregrinosMapper.toEntity(returnedPeregrinosDTO);
        assertPeregrinosUpdatableFieldsEquals(returnedPeregrinos, getPersistedPeregrinos(returnedPeregrinos));

        insertedPeregrinos = returnedPeregrinos;
    }

    @Test
    @Transactional
    void createPeregrinosWithExistingId() throws Exception {
        // Create the Peregrinos with an existing ID
        peregrinos.setId(1L);
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeregrinosMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApellidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        peregrinos.setApellido(null);

        // Create the Peregrinos, which fails.
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        restPeregrinosMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        peregrinos.setNombre(null);

        // Create the Peregrinos, which fails.
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        restPeregrinosMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMayorMenorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        peregrinos.setMayorMenor(null);

        // Create the Peregrinos, which fails.
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        restPeregrinosMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalidaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        peregrinos.setSalida(null);

        // Create the Peregrinos, which fails.
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        restPeregrinosMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormaPagoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        peregrinos.setFormaPago(null);

        // Create the Peregrinos, which fails.
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        restPeregrinosMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeregrinos() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList
        restPeregrinosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peregrinos.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEspecial").value(hasItem(DEFAULT_NUMERO_ESPECIAL)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].numeroDocumento").value(hasItem(DEFAULT_NUMERO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].mayorMenor").value(hasItem(DEFAULT_MAYOR_MENOR.toString())))
            .andExpect(jsonPath("$.[*].salida").value(hasItem(DEFAULT_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].pago").value(hasItem(DEFAULT_P_AGO)))
            .andExpect(jsonPath("$.[*].formaPago").value(hasItem(DEFAULT_FORMA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].aclaraciones").value(hasItem(DEFAULT_ACLARACIONES)))
            .andExpect(jsonPath("$.[*].completoFormulario").value(hasItem(DEFAULT_COMPLETO_FORMULARIO)));
    }

    @Test
    @Transactional
    void getPeregrinos() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get the peregrinos
        restPeregrinosMockMvc
            .perform(get(ENTITY_API_URL_ID, peregrinos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(peregrinos.getId().intValue()))
            .andExpect(jsonPath("$.numeroEspecial").value(DEFAULT_NUMERO_ESPECIAL))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.numeroDocumento").value(DEFAULT_NUMERO_DOCUMENTO))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.mayorMenor").value(DEFAULT_MAYOR_MENOR.toString()))
            .andExpect(jsonPath("$.salida").value(DEFAULT_SALIDA.toString()))
            .andExpect(jsonPath("$.pago").value(DEFAULT_P_AGO))
            .andExpect(jsonPath("$.formaPago").value(DEFAULT_FORMA_PAGO.toString()))
            .andExpect(jsonPath("$.aclaraciones").value(DEFAULT_ACLARACIONES))
            .andExpect(jsonPath("$.completoFormulario").value(DEFAULT_COMPLETO_FORMULARIO));
    }

    @Test
    @Transactional
    void getPeregrinosByIdFiltering() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        Long id = peregrinos.getId();

        defaultPeregrinosFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPeregrinosFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPeregrinosFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial equals to
        defaultPeregrinosFiltering("numeroEspecial.equals=" + DEFAULT_NUMERO_ESPECIAL, "numeroEspecial.equals=" + UPDATED_NUMERO_ESPECIAL);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial in
        defaultPeregrinosFiltering(
            "numeroEspecial.in=" + DEFAULT_NUMERO_ESPECIAL + "," + UPDATED_NUMERO_ESPECIAL,
            "numeroEspecial.in=" + UPDATED_NUMERO_ESPECIAL
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial is not null
        defaultPeregrinosFiltering("numeroEspecial.specified=true", "numeroEspecial.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial is greater than or equal to
        defaultPeregrinosFiltering(
            "numeroEspecial.greaterThanOrEqual=" + DEFAULT_NUMERO_ESPECIAL,
            "numeroEspecial.greaterThanOrEqual=" + UPDATED_NUMERO_ESPECIAL
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial is less than or equal to
        defaultPeregrinosFiltering(
            "numeroEspecial.lessThanOrEqual=" + DEFAULT_NUMERO_ESPECIAL,
            "numeroEspecial.lessThanOrEqual=" + SMALLER_NUMERO_ESPECIAL
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial is less than
        defaultPeregrinosFiltering(
            "numeroEspecial.lessThan=" + UPDATED_NUMERO_ESPECIAL,
            "numeroEspecial.lessThan=" + DEFAULT_NUMERO_ESPECIAL
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroEspecialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroEspecial is greater than
        defaultPeregrinosFiltering(
            "numeroEspecial.greaterThan=" + SMALLER_NUMERO_ESPECIAL,
            "numeroEspecial.greaterThan=" + DEFAULT_NUMERO_ESPECIAL
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where apellido equals to
        defaultPeregrinosFiltering("apellido.equals=" + DEFAULT_APELLIDO, "apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where apellido in
        defaultPeregrinosFiltering("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO, "apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where apellido is not null
        defaultPeregrinosFiltering("apellido.specified=true", "apellido.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByApellidoContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where apellido contains
        defaultPeregrinosFiltering("apellido.contains=" + DEFAULT_APELLIDO, "apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where apellido does not contain
        defaultPeregrinosFiltering("apellido.doesNotContain=" + UPDATED_APELLIDO, "apellido.doesNotContain=" + DEFAULT_APELLIDO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where nombre equals to
        defaultPeregrinosFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where nombre in
        defaultPeregrinosFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where nombre is not null
        defaultPeregrinosFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where nombre contains
        defaultPeregrinosFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where nombre does not contain
        defaultPeregrinosFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroDocumento equals to
        defaultPeregrinosFiltering(
            "numeroDocumento.equals=" + DEFAULT_NUMERO_DOCUMENTO,
            "numeroDocumento.equals=" + UPDATED_NUMERO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroDocumento in
        defaultPeregrinosFiltering(
            "numeroDocumento.in=" + DEFAULT_NUMERO_DOCUMENTO + "," + UPDATED_NUMERO_DOCUMENTO,
            "numeroDocumento.in=" + UPDATED_NUMERO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroDocumento is not null
        defaultPeregrinosFiltering("numeroDocumento.specified=true", "numeroDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroDocumentoContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroDocumento contains
        defaultPeregrinosFiltering(
            "numeroDocumento.contains=" + DEFAULT_NUMERO_DOCUMENTO,
            "numeroDocumento.contains=" + UPDATED_NUMERO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByNumeroDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where numeroDocumento does not contain
        defaultPeregrinosFiltering(
            "numeroDocumento.doesNotContain=" + UPDATED_NUMERO_DOCUMENTO,
            "numeroDocumento.doesNotContain=" + DEFAULT_NUMERO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where telefono equals to
        defaultPeregrinosFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where telefono in
        defaultPeregrinosFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where telefono is not null
        defaultPeregrinosFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where telefono contains
        defaultPeregrinosFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where telefono does not contain
        defaultPeregrinosFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByMayorMenorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where mayorMenor equals to
        defaultPeregrinosFiltering("mayorMenor.equals=" + DEFAULT_MAYOR_MENOR, "mayorMenor.equals=" + UPDATED_MAYOR_MENOR);
    }

    @Test
    @Transactional
    void getAllPeregrinosByMayorMenorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where mayorMenor in
        defaultPeregrinosFiltering(
            "mayorMenor.in=" + DEFAULT_MAYOR_MENOR + "," + UPDATED_MAYOR_MENOR,
            "mayorMenor.in=" + UPDATED_MAYOR_MENOR
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByMayorMenorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where mayorMenor is not null
        defaultPeregrinosFiltering("mayorMenor.specified=true", "mayorMenor.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosBySalidaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where salida equals to
        defaultPeregrinosFiltering("salida.equals=" + DEFAULT_SALIDA, "salida.equals=" + UPDATED_SALIDA);
    }

    @Test
    @Transactional
    void getAllPeregrinosBySalidaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where salida in
        defaultPeregrinosFiltering("salida.in=" + DEFAULT_SALIDA + "," + UPDATED_SALIDA, "salida.in=" + UPDATED_SALIDA);
    }

    @Test
    @Transactional
    void getAllPeregrinosBySalidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where salida is not null
        defaultPeregrinosFiltering("salida.specified=true", "salida.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago equals to
        defaultPeregrinosFiltering("pago.equals=" + DEFAULT_P_AGO, "pago.equals=" + UPDATED_P_AGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago in
        defaultPeregrinosFiltering("pago.in=" + DEFAULT_P_AGO + "," + UPDATED_P_AGO, "pago.in=" + UPDATED_P_AGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago is not null
        defaultPeregrinosFiltering("pago.specified=true", "pago.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago is greater than or equal to
        defaultPeregrinosFiltering("pago.greaterThanOrEqual=" + DEFAULT_P_AGO, "pago.greaterThanOrEqual=" + UPDATED_P_AGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago is less than or equal to
        defaultPeregrinosFiltering("pago.lessThanOrEqual=" + DEFAULT_P_AGO, "pago.lessThanOrEqual=" + SMALLER_P_AGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago is less than
        defaultPeregrinosFiltering("pago.lessThan=" + UPDATED_P_AGO, "pago.lessThan=" + DEFAULT_P_AGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByPagoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where pago is greater than
        defaultPeregrinosFiltering("pago.greaterThan=" + SMALLER_P_AGO, "pago.greaterThan=" + DEFAULT_P_AGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByFormaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where formaPago equals to
        defaultPeregrinosFiltering("formaPago.equals=" + DEFAULT_FORMA_PAGO, "formaPago.equals=" + UPDATED_FORMA_PAGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByFormaPagoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where formaPago in
        defaultPeregrinosFiltering("formaPago.in=" + DEFAULT_FORMA_PAGO + "," + UPDATED_FORMA_PAGO, "formaPago.in=" + UPDATED_FORMA_PAGO);
    }

    @Test
    @Transactional
    void getAllPeregrinosByFormaPagoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where formaPago is not null
        defaultPeregrinosFiltering("formaPago.specified=true", "formaPago.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByAclaracionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where aclaraciones equals to
        defaultPeregrinosFiltering("aclaraciones.equals=" + DEFAULT_ACLARACIONES, "aclaraciones.equals=" + UPDATED_ACLARACIONES);
    }

    @Test
    @Transactional
    void getAllPeregrinosByAclaracionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where aclaraciones in
        defaultPeregrinosFiltering(
            "aclaraciones.in=" + DEFAULT_ACLARACIONES + "," + UPDATED_ACLARACIONES,
            "aclaraciones.in=" + UPDATED_ACLARACIONES
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByAclaracionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where aclaraciones is not null
        defaultPeregrinosFiltering("aclaraciones.specified=true", "aclaraciones.specified=false");
    }

    @Test
    @Transactional
    void getAllPeregrinosByAclaracionesContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where aclaraciones contains
        defaultPeregrinosFiltering("aclaraciones.contains=" + DEFAULT_ACLARACIONES, "aclaraciones.contains=" + UPDATED_ACLARACIONES);
    }

    @Test
    @Transactional
    void getAllPeregrinosByAclaracionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where aclaraciones does not contain
        defaultPeregrinosFiltering(
            "aclaraciones.doesNotContain=" + UPDATED_ACLARACIONES,
            "aclaraciones.doesNotContain=" + DEFAULT_ACLARACIONES
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByCompletoFormularioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where completoFormulario equals to
        defaultPeregrinosFiltering(
            "completoFormulario.equals=" + DEFAULT_COMPLETO_FORMULARIO,
            "completoFormulario.equals=" + UPDATED_COMPLETO_FORMULARIO
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByCompletoFormularioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where completoFormulario in
        defaultPeregrinosFiltering(
            "completoFormulario.in=" + DEFAULT_COMPLETO_FORMULARIO + "," + UPDATED_COMPLETO_FORMULARIO,
            "completoFormulario.in=" + UPDATED_COMPLETO_FORMULARIO
        );
    }

    @Test
    @Transactional
    void getAllPeregrinosByCompletoFormularioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        // Get all the peregrinosList where completoFormulario is not null
        defaultPeregrinosFiltering("completoFormulario.specified=true", "completoFormulario.specified=false");
    }

    private void defaultPeregrinosFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPeregrinosShouldBeFound(shouldBeFound);
        defaultPeregrinosShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeregrinosShouldBeFound(String filter) throws Exception {
        restPeregrinosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peregrinos.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroEspecial").value(hasItem(DEFAULT_NUMERO_ESPECIAL)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].numeroDocumento").value(hasItem(DEFAULT_NUMERO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].mayorMenor").value(hasItem(DEFAULT_MAYOR_MENOR.toString())))
            .andExpect(jsonPath("$.[*].salida").value(hasItem(DEFAULT_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].pago").value(hasItem(DEFAULT_P_AGO)))
            .andExpect(jsonPath("$.[*].formaPago").value(hasItem(DEFAULT_FORMA_PAGO.toString())))
            .andExpect(jsonPath("$.[*].aclaraciones").value(hasItem(DEFAULT_ACLARACIONES)))
            .andExpect(jsonPath("$.[*].completoFormulario").value(hasItem(DEFAULT_COMPLETO_FORMULARIO)));

        // Check, that the count call also returns 1
        restPeregrinosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeregrinosShouldNotBeFound(String filter) throws Exception {
        restPeregrinosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeregrinosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPeregrinos() throws Exception {
        // Get the peregrinos
        restPeregrinosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeregrinos() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the peregrinos
        Peregrinos updatedPeregrinos = peregrinosRepository.findById(peregrinos.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPeregrinos are not directly saved in db
        em.detach(updatedPeregrinos);
        updatedPeregrinos
            .numeroEspecial(UPDATED_NUMERO_ESPECIAL)
            .apellido(UPDATED_APELLIDO)
            .nombre(UPDATED_NOMBRE)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .telefono(UPDATED_TELEFONO)
            .mayorMenor(UPDATED_MAYOR_MENOR)
            .salida(UPDATED_SALIDA)
            .pago(UPDATED_P_AGO)
            .formaPago(UPDATED_FORMA_PAGO)
            .aclaraciones(UPDATED_ACLARACIONES)
            .completoFormulario(UPDATED_COMPLETO_FORMULARIO);
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(updatedPeregrinos);

        restPeregrinosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peregrinosDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(peregrinosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPeregrinosToMatchAllProperties(updatedPeregrinos);
    }

    @Test
    @Transactional
    void putNonExistingPeregrinos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peregrinos.setId(longCount.incrementAndGet());

        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeregrinosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peregrinosDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(peregrinosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeregrinos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peregrinos.setId(longCount.incrementAndGet());

        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeregrinosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(peregrinosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeregrinos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peregrinos.setId(longCount.incrementAndGet());

        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeregrinosMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(peregrinosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeregrinosWithPatch() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the peregrinos using partial update
        Peregrinos partialUpdatedPeregrinos = new Peregrinos();
        partialUpdatedPeregrinos.setId(peregrinos.getId());

        partialUpdatedPeregrinos
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .telefono(UPDATED_TELEFONO)
            .salida(UPDATED_SALIDA)
            .formaPago(UPDATED_FORMA_PAGO)
            .aclaraciones(UPDATED_ACLARACIONES)
            .completoFormulario(UPDATED_COMPLETO_FORMULARIO);

        restPeregrinosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeregrinos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeregrinos))
            )
            .andExpect(status().isOk());

        // Validate the Peregrinos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeregrinosUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPeregrinos, peregrinos),
            getPersistedPeregrinos(peregrinos)
        );
    }

    @Test
    @Transactional
    void fullUpdatePeregrinosWithPatch() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the peregrinos using partial update
        Peregrinos partialUpdatedPeregrinos = new Peregrinos();
        partialUpdatedPeregrinos.setId(peregrinos.getId());

        partialUpdatedPeregrinos
            .numeroEspecial(UPDATED_NUMERO_ESPECIAL)
            .apellido(UPDATED_APELLIDO)
            .nombre(UPDATED_NOMBRE)
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .telefono(UPDATED_TELEFONO)
            .mayorMenor(UPDATED_MAYOR_MENOR)
            .salida(UPDATED_SALIDA)
            .pago(UPDATED_P_AGO)
            .formaPago(UPDATED_FORMA_PAGO)
            .aclaraciones(UPDATED_ACLARACIONES)
            .completoFormulario(UPDATED_COMPLETO_FORMULARIO);

        restPeregrinosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeregrinos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeregrinos))
            )
            .andExpect(status().isOk());

        // Validate the Peregrinos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeregrinosUpdatableFieldsEquals(partialUpdatedPeregrinos, getPersistedPeregrinos(partialUpdatedPeregrinos));
    }

    @Test
    @Transactional
    void patchNonExistingPeregrinos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peregrinos.setId(longCount.incrementAndGet());

        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeregrinosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peregrinosDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(peregrinosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeregrinos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peregrinos.setId(longCount.incrementAndGet());

        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeregrinosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(peregrinosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeregrinos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        peregrinos.setId(longCount.incrementAndGet());

        // Create the Peregrinos
        PeregrinosDTO peregrinosDTO = peregrinosMapper.toDto(peregrinos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeregrinosMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(peregrinosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peregrinos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeregrinos() throws Exception {
        // Initialize the database
        insertedPeregrinos = peregrinosRepository.saveAndFlush(peregrinos);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the peregrinos
        restPeregrinosMockMvc
            .perform(delete(ENTITY_API_URL_ID, peregrinos.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return peregrinosRepository.count();
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

    protected Peregrinos getPersistedPeregrinos(Peregrinos peregrinos) {
        return peregrinosRepository.findById(peregrinos.getId()).orElseThrow();
    }

    protected void assertPersistedPeregrinosToMatchAllProperties(Peregrinos expectedPeregrinos) {
        assertPeregrinosAllPropertiesEquals(expectedPeregrinos, getPersistedPeregrinos(expectedPeregrinos));
    }

    protected void assertPersistedPeregrinosToMatchUpdatableProperties(Peregrinos expectedPeregrinos) {
        assertPeregrinosAllUpdatablePropertiesEquals(expectedPeregrinos, getPersistedPeregrinos(expectedPeregrinos));
    }
}
