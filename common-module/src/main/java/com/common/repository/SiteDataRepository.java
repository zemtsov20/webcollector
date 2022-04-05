package com.common.repository;

import com.common.entity.SiteData;
import com.common.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SiteDataRepository extends JpaRepository<SiteData, Date> {
    @Query(value = "SELECT s FROM SiteData s WHERE s.state = ?1")
    List<SiteData> findByState(State state);
}
