package com.psmsf.lujanapp.service.mapper;

import static com.psmsf.lujanapp.domain.PeregrinosAsserts.*;
import static com.psmsf.lujanapp.domain.PeregrinosTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeregrinosMapperTest {

    private PeregrinosMapper peregrinosMapper;

    @BeforeEach
    void setUp() {
        peregrinosMapper = new PeregrinosMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPeregrinosSample1();
        var actual = peregrinosMapper.toEntity(peregrinosMapper.toDto(expected));
        assertPeregrinosAllPropertiesEquals(expected, actual);
    }
}
