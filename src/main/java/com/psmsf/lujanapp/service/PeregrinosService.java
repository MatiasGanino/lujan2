package com.psmsf.lujanapp.service;

import com.psmsf.lujanapp.domain.Peregrinos;
import com.psmsf.lujanapp.repository.PeregrinosRepository;
import com.psmsf.lujanapp.service.dto.PeregrinosDTO;
import com.psmsf.lujanapp.service.mapper.PeregrinosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.psmsf.lujanapp.domain.Peregrinos}.
 */
@Service
@Transactional
public class PeregrinosService {

    private static final Logger LOG = LoggerFactory.getLogger(PeregrinosService.class);

    private final PeregrinosRepository peregrinosRepository;

    private final PeregrinosMapper peregrinosMapper;

    public PeregrinosService(PeregrinosRepository peregrinosRepository, PeregrinosMapper peregrinosMapper) {
        this.peregrinosRepository = peregrinosRepository;
        this.peregrinosMapper = peregrinosMapper;
    }

    /**
     * Save a peregrinos.
     *
     * @param peregrinosDTO the entity to save.
     * @return the persisted entity.
     */
    public PeregrinosDTO save(PeregrinosDTO peregrinosDTO) {
        LOG.debug("Request to save Peregrinos : {}", peregrinosDTO);
        Peregrinos peregrinos = peregrinosMapper.toEntity(peregrinosDTO);
        peregrinos = peregrinosRepository.save(peregrinos);
        return peregrinosMapper.toDto(peregrinos);
    }

    /**
     * Update a peregrinos.
     *
     * @param peregrinosDTO the entity to save.
     * @return the persisted entity.
     */
    public PeregrinosDTO update(PeregrinosDTO peregrinosDTO) {
        LOG.debug("Request to update Peregrinos : {}", peregrinosDTO);
        Peregrinos peregrinos = peregrinosMapper.toEntity(peregrinosDTO);
        peregrinos = peregrinosRepository.save(peregrinos);
        return peregrinosMapper.toDto(peregrinos);
    }

    /**
     * Partially update a peregrinos.
     *
     * @param peregrinosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PeregrinosDTO> partialUpdate(PeregrinosDTO peregrinosDTO) {
        LOG.debug("Request to partially update Peregrinos : {}", peregrinosDTO);

        return peregrinosRepository
            .findById(peregrinosDTO.getId())
            .map(existingPeregrinos -> {
                peregrinosMapper.partialUpdate(existingPeregrinos, peregrinosDTO);

                return existingPeregrinos;
            })
            .map(peregrinosRepository::save)
            .map(peregrinosMapper::toDto);
    }

    /**
     * Get one peregrinos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeregrinosDTO> findOne(Long id) {
        LOG.debug("Request to get Peregrinos : {}", id);
        return peregrinosRepository.findById(id).map(peregrinosMapper::toDto);
    }

    /**
     * Delete the peregrinos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Peregrinos : {}", id);
        peregrinosRepository.deleteById(id);
    }
}
