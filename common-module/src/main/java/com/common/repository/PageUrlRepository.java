package com.common.repository;

import com.common.entity.PageUrl;
import com.common.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageUrlRepository extends JpaRepository<PageUrl, Long> {
    @Query(value = "SELECT u FROM PageUrl u WHERE u.state = ?1")
    List<PageUrl> findByState(/*@Param("state")*/ State state);

    @Query(value = "SELECT u FROM PageUrl u WHERE u.state = ?1 AND u.hasNoChild = ?2")
    List<PageUrl> findByStateAndChild(/*@Param("state")*/ State state, boolean hasNoChild);
}
