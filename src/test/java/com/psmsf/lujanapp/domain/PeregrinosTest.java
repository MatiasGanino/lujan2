package com.psmsf.lujanapp.domain;

import static com.psmsf.lujanapp.domain.PeregrinosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.psmsf.lujanapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeregrinosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peregrinos.class);
        Peregrinos peregrinos1 = getPeregrinosSample1();
        Peregrinos peregrinos2 = new Peregrinos();
        assertThat(peregrinos1).isNotEqualTo(peregrinos2);

        peregrinos2.setId(peregrinos1.getId());
        assertThat(peregrinos1).isEqualTo(peregrinos2);

        peregrinos2 = getPeregrinosSample2();
        assertThat(peregrinos1).isNotEqualTo(peregrinos2);
    }
}
