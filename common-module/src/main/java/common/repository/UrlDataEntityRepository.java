package common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// mb taking a group of entities will be faster
@Repository
public interface UrlDataEntityRepository  extends CrudRepository<UrlDataEntity, Long> {
    @Query( value = "select * from public.url_data_entity \n" +
                    "where url_data_entity.state = 0 \n" +
//                    "order by url_data_entity.url_ts asc \n" +
                    "limit 10 \n",
            nativeQuery = true)
    public List<UrlDataEntity> findUnchecked();

}
