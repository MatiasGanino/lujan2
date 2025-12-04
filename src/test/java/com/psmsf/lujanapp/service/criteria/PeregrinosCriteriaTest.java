package com.psmsf.lujanapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PeregrinosCriteriaTest {

    @Test
    void newPeregrinosCriteriaHasAllFiltersNullTest() {
        var peregrinosCriteria = new PeregrinosCriteria();
        assertThat(peregrinosCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void peregrinosCriteriaFluentMethodsCreatesFiltersTest() {
        var peregrinosCriteria = new PeregrinosCriteria();

        setAllFilters(peregrinosCriteria);

        assertThat(peregrinosCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void peregrinosCriteriaCopyCreatesNullFilterTest() {
        var peregrinosCriteria = new PeregrinosCriteria();
        var copy = peregrinosCriteria.copy();

        assertThat(peregrinosCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(peregrinosCriteria)
        );
    }

    @Test
    void peregrinosCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var peregrinosCriteria = new PeregrinosCriteria();
        setAllFilters(peregrinosCriteria);

        var copy = peregrinosCriteria.copy();

        assertThat(peregrinosCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(peregrinosCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var peregrinosCriteria = new PeregrinosCriteria();

        assertThat(peregrinosCriteria).hasToString("PeregrinosCriteria{}");
    }

    private static void setAllFilters(PeregrinosCriteria peregrinosCriteria) {
        peregrinosCriteria.id();
        peregrinosCriteria.numeroEspecial();
        peregrinosCriteria.apellido();
        peregrinosCriteria.nombre();
        peregrinosCriteria.numeroDocumento();
        peregrinosCriteria.telefono();
        peregrinosCriteria.mayorMenor();
        peregrinosCriteria.salida();
        peregrinosCriteria.pago();
        peregrinosCriteria.formaPago();
        peregrinosCriteria.aclaraciones();
        peregrinosCriteria.completoFormulario();
        peregrinosCriteria.distinct();
    }

    private static Condition<PeregrinosCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumeroEspecial()) &&
                condition.apply(criteria.getApellido()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getNumeroDocumento()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getMayorMenor()) &&
                condition.apply(criteria.getSalida()) &&
                condition.apply(criteria.getPago()) &&
                condition.apply(criteria.getFormaPago()) &&
                condition.apply(criteria.getAclaraciones()) &&
                condition.apply(criteria.getCompletoFormulario()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PeregrinosCriteria> copyFiltersAre(PeregrinosCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumeroEspecial(), copy.getNumeroEspecial()) &&
                condition.apply(criteria.getApellido(), copy.getApellido()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getNumeroDocumento(), copy.getNumeroDocumento()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getMayorMenor(), copy.getMayorMenor()) &&
                condition.apply(criteria.getSalida(), copy.getSalida()) &&
                condition.apply(criteria.getPago(), copy.getPago()) &&
                condition.apply(criteria.getFormaPago(), copy.getFormaPago()) &&
                condition.apply(criteria.getAclaraciones(), copy.getAclaraciones()) &&
                condition.apply(criteria.getCompletoFormulario(), copy.getCompletoFormulario()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
