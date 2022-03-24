package com.common.repository;

import com.common.entity.UrlData;
import com.common.enums.HtmlState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// mb taking a group of entities will be faster
@Repository
public interface UrlDataRepository extends JpaRepository<UrlData, Long> {
//    @Query( value = "select * from public.url_data_entity \n" +
//                    "where url_data_entity.state = 0 \n" +
////                    "order by url_data_entity.url_ts asc \n" +
//                    "limit 10 \n",
//            nativeQuery = true)
//    public List<UrlDataEntity> findUnchecked();

    @Query(value = "select s from UrlData s where s.state = ?1")
    List<UrlData> findByState(HtmlState state);
}
