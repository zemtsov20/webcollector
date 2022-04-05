package com.common.repository;

import com.common.entity.CategoryData;
import com.common.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CategoryDataRepository extends JpaRepository<CategoryData, Long> {

    @Query(value = "SELECT s FROM CategoryData s WHERE s.state = ?1")
    List<CategoryData> findByState(State state);
}
