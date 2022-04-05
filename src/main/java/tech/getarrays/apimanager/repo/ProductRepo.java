package tech.getarrays.apimanager.repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tech.getarrays.apimanager.model.Product;


import java.util.Optional;

public interface ProductRepo extends CrudRepository<Product,Long>,JpaRepository<Product,Long>, JpaSpecificationExecutor<Product>, PagingAndSortingRepository<Product, Long> {
    void deleteProductById(Long id);
    Optional <Product>findProductById(Long id);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryContaining(String category, Pageable pageable);
    Page<Product> findProductByNameContainingAndCategoryContaining(String name,
                                                                  String category,
                                                                  Pageable pageable);
}
