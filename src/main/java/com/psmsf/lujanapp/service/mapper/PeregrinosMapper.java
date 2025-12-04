package com.psmsf.lujanapp.service.mapper;

import com.psmsf.lujanapp.domain.Peregrinos;
import com.psmsf.lujanapp.service.dto.PeregrinosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Peregrinos} and its DTO {@link PeregrinosDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeregrinosMapper extends EntityMapper<PeregrinosDTO, Peregrinos> {}
