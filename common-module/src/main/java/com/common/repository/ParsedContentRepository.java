package com.common.repository;

import com.common.entity.ParsedContent;
import com.common.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParsedContentRepository extends JpaRepository<ParsedContent, Long> {
    @Query(value = "SELECT s FROM ParsedContent s WHERE s.state = ?1")
    List<ParsedContent> findByState(State state);
}
