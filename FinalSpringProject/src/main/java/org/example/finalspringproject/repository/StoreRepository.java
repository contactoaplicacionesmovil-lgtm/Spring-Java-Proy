package org.example.finalspringproject.repository;

import org.example.finalspringproject.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("""
        select s
        from Store s
        where lower(s.name) like lower(concat('%', :subName, '%'))
        """)
    List<Store> findBySubName(@Param("subName") String subName);
}
