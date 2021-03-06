package com.kakaopay.api.domain.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {

    @Query("select support from Support support join support.region region where region.name=:region")
    List<Support> findByList(@Param("region") String region);
}
