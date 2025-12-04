package com.psmsf.lujanapp.service;

import com.psmsf.lujanapp.domain.*; // for static metamodels
import com.psmsf.lujanapp.domain.Peregrinos;
import com.psmsf.lujanapp.repository.PeregrinosRepository;
import com.psmsf.lujanapp.service.criteria.PeregrinosCriteria;
import com.psmsf.lujanapp.service.dto.PeregrinosDTO;
import com.psmsf.lujanapp.service.mapper.PeregrinosMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Peregrinos} entities in the database.
 * The main input is a {@link PeregrinosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PeregrinosDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeregrinosQueryService extends QueryService<Peregrinos> {

    private static final Logger LOG = LoggerFactory.getLogger(PeregrinosQueryService.class);

    private final PeregrinosRepository peregrinosRepository;

    private final PeregrinosMapper peregrinosMapper;

    public PeregrinosQueryService(PeregrinosRepository peregrinosRepository, PeregrinosMapper peregrinosMapper) {
        this.peregrinosRepository = peregrinosRepository;
        this.peregrinosMapper = peregrinosMapper;
    }

    /**
     * Return a {@link Page} of {@link PeregrinosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeregrinosDTO> findByCriteria(PeregrinosCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Peregrinos> specification = createSpecification(criteria);
        return peregrinosRepository.findAll(specification, page).map(peregrinosMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeregrinosCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Peregrinos> specification = createSpecification(criteria);
        return peregrinosRepository.count(specification);
    }

    /**
     * Function to convert {@link PeregrinosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Peregrinos> createSpecification(PeregrinosCriteria criteria) {
        Specification<Peregrinos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Peregrinos_.id),
                buildRangeSpecification(criteria.getNumeroEspecial(), Peregrinos_.numeroEspecial),
                buildStringSpecification(criteria.getApellido(), Peregrinos_.apellido),
                buildStringSpecification(criteria.getNombre(), Peregrinos_.nombre),
                buildStringSpecification(criteria.getNumeroDocumento(), Peregrinos_.numeroDocumento),
                buildStringSpecification(criteria.getTelefono(), Peregrinos_.telefono),
                buildSpecification(criteria.getMayorMenor(), Peregrinos_.mayorMenor),
                buildSpecification(criteria.getSalida(), Peregrinos_.salida),
                buildRangeSpecification(criteria.getPago(), Peregrinos_.pago),
                buildSpecification(criteria.getFormaPago(), Peregrinos_.formaPago),
                buildStringSpecification(criteria.getAclaraciones(), Peregrinos_.aclaraciones),
                buildSpecification(criteria.getCompletoFormulario(), Peregrinos_.completoFormulario)
            );
        }
        return specification;
    }
}
