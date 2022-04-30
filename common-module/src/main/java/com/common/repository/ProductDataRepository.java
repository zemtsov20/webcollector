package com.common.repository;

import com.common.entity.ProductData;
import com.common.enums.State;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT s FROM ProductData s")
    @QueryHints(@QueryHint(name = AvailableSettings.JPA_LOCK_TIMEOUT, value = "-2"/*SKIP_LOCKED constant*/))
    List<ProductData> findByState(State state, Pageable pageable);
}
