package org.example.finalspringproject.repository;

import org.example.finalspringproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    Optional<Product> findBySku(String sku);
    List<Product> findByName(String name);
    List<Product> findByNameAndCategory(String name, String category);

    @Query("""
        select distinct p
        from Product p
        join p.inventories i
        where lower(p.category) = lower(:category)
          and i.store.id = :storeId
        """)
    List<Product> findByCategoryAndStoreId(@Param("category") String category, @Param("storeId") Long storeId);

    @Query("""
        select distinct p
        from Product p
        join p.inventories i
        where i.store.id = :storeId
          and lower(p.name) like lower(concat('%', :name, '%'))
        """)
    List<Product> findByNameLikeAndStoreId(@Param("name") String name, @Param("storeId") Long storeId);

    @Query("""
        select p
        from Product p
        where lower(p.name) like lower(concat('%', :subName, '%'))
        """)
    List<Product> findProductBySubName(@Param("subName") String subName);

    @Query("""
        select distinct p
        from Product p
        join p.inventories i
        where i.store.id = :storeId
        """)
    List<Product> findProductsByStoreId(@Param("storeId") Long storeId);

    @Query("""
        select p
        from Product p
        where lower(p.category) = lower(:category)
        """)
    List<Product> findProductByCategory(@Param("category") String category);

    @Query("""
        select p
        from Product p
        where lower(p.name) like lower(concat('%', :subName, '%'))
          and lower(p.category) = lower(:category)
        """)
    List<Product> findProductBySubNameAndCategory(@Param("subName") String subName, @Param("category") String category);
}
