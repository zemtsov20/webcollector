package com.common.repository;

import com.common.entity.SiteData;
import com.common.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

@Repository
public interface SiteDataRepository extends JpaRepository<SiteData, Date> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT s FROM SiteData s WHERE s.state = ?1")
    List<SiteData> findAndLockByState(State state);
}
