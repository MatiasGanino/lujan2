package com.psmsf.lujanapp.repository;

import com.psmsf.lujanapp.domain.Peregrinos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Peregrinos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeregrinosRepository extends JpaRepository<Peregrinos, Long>, JpaSpecificationExecutor<Peregrinos> {}
