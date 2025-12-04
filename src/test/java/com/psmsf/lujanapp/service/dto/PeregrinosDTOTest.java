package com.psmsf.lujanapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.psmsf.lujanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeregrinosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeregrinosDTO.class);
        PeregrinosDTO peregrinosDTO1 = new PeregrinosDTO();
        peregrinosDTO1.setId(1L);
        PeregrinosDTO peregrinosDTO2 = new PeregrinosDTO();
        assertThat(peregrinosDTO1).isNotEqualTo(peregrinosDTO2);
        peregrinosDTO2.setId(peregrinosDTO1.getId());
        assertThat(peregrinosDTO1).isEqualTo(peregrinosDTO2);
        peregrinosDTO2.setId(2L);
        assertThat(peregrinosDTO1).isNotEqualTo(peregrinosDTO2);
        peregrinosDTO1.setId(null);
        assertThat(peregrinosDTO1).isNotEqualTo(peregrinosDTO2);
    }
}
