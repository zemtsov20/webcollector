package com.common.repository;

import com.common.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlEntityRepository extends JpaRepository<UrlEntity, Long> {
}
