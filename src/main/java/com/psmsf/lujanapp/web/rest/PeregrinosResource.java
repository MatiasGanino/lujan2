package com.psmsf.lujanapp.web.rest;

import com.psmsf.lujanapp.repository.PeregrinosRepository;
import com.psmsf.lujanapp.service.PeregrinosQueryService;
import com.psmsf.lujanapp.service.PeregrinosService;
import com.psmsf.lujanapp.service.criteria.PeregrinosCriteria;
import com.psmsf.lujanapp.service.dto.PeregrinosDTO;
import com.psmsf.lujanapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.psmsf.lujanapp.domain.Peregrinos}.
 */
@RestController
@RequestMapping("/api/peregrinos")
public class PeregrinosResource {

    private static final Logger LOG = LoggerFactory.getLogger(PeregrinosResource.class);

    private static final String ENTITY_NAME = "peregrinos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeregrinosService peregrinosService;

    private final PeregrinosRepository peregrinosRepository;

    private final PeregrinosQueryService peregrinosQueryService;

    public PeregrinosResource(
        PeregrinosService peregrinosService,
        PeregrinosRepository peregrinosRepository,
        PeregrinosQueryService peregrinosQueryService
    ) {
        this.peregrinosService = peregrinosService;
        this.peregrinosRepository = peregrinosRepository;
        this.peregrinosQueryService = peregrinosQueryService;
    }

    /**
     * {@code POST  /peregrinos} : Create a new peregrinos.
     *
     * @param peregrinosDTO the peregrinosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peregrinosDTO, or with status {@code 400 (Bad Request)} if the peregrinos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PeregrinosDTO> createPeregrinos(@Valid @RequestBody PeregrinosDTO peregrinosDTO) throws URISyntaxException {
        LOG.debug("REST request to save Peregrinos : {}", peregrinosDTO);
        if (peregrinosDTO.getId() != null) {
            throw new BadRequestAlertException("A new peregrinos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        peregrinosDTO = peregrinosService.save(peregrinosDTO);
        return ResponseEntity.created(new URI("/api/peregrinos/" + peregrinosDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, peregrinosDTO.getId().toString()))
            .body(peregrinosDTO);
    }

    /**
     * {@code PUT  /peregrinos/:id} : Updates an existing peregrinos.
     *
     * @param id the id of the peregrinosDTO to save.
     * @param peregrinosDTO the peregrinosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peregrinosDTO,
     * or with status {@code 400 (Bad Request)} if the peregrinosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peregrinosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PeregrinosDTO> updatePeregrinos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeregrinosDTO peregrinosDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Peregrinos : {}, {}", id, peregrinosDTO);
        if (peregrinosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peregrinosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peregrinosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        peregrinosDTO = peregrinosService.update(peregrinosDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peregrinosDTO.getId().toString()))
            .body(peregrinosDTO);
    }

    /**
     * {@code PATCH  /peregrinos/:id} : Partial updates given fields of an existing peregrinos, field will ignore if it is null
     *
     * @param id the id of the peregrinosDTO to save.
     * @param peregrinosDTO the peregrinosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peregrinosDTO,
     * or with status {@code 400 (Bad Request)} if the peregrinosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the peregrinosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the peregrinosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeregrinosDTO> partialUpdatePeregrinos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeregrinosDTO peregrinosDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Peregrinos partially : {}, {}", id, peregrinosDTO);
        if (peregrinosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peregrinosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peregrinosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeregrinosDTO> result = peregrinosService.partialUpdate(peregrinosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peregrinosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /peregrinos} : get all the peregrinos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peregrinos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PeregrinosDTO>> getAllPeregrinos(
        PeregrinosCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Peregrinos by criteria: {}", criteria);

        Page<PeregrinosDTO> page = peregrinosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /peregrinos/count} : count all the peregrinos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPeregrinos(PeregrinosCriteria criteria) {
        LOG.debug("REST request to count Peregrinos by criteria: {}", criteria);
        return ResponseEntity.ok().body(peregrinosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /peregrinos/:id} : get the "id" peregrinos.
     *
     * @param id the id of the peregrinosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peregrinosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PeregrinosDTO> getPeregrinos(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Peregrinos : {}", id);
        Optional<PeregrinosDTO> peregrinosDTO = peregrinosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peregrinosDTO);
    }

    /**
     * {@code DELETE  /peregrinos/:id} : delete the "id" peregrinos.
     *
     * @param id the id of the peregrinosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeregrinos(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Peregrinos : {}", id);
        peregrinosService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
