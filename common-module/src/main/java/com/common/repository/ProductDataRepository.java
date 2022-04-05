package com.common.repository;

import com.common.entity.ProductData;
import com.common.enums.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Long> {
    @Query(value = "SELECT s FROM ProductData s WHERE s.state = ?1")
    List<ProductData> findByState(State state, Pageable pageable);
}
