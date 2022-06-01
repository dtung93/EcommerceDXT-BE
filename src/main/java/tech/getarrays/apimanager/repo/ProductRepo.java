package tech.getarrays.apimanager.repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tech.getarrays.apimanager.model.Product;


import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProductRepo extends CrudRepository<Product,Long>,JpaRepository<Product,Long>, JpaSpecificationExecutor<Product>, PagingAndSortingRepository<Product, Long> {

    List<Product> findAll();

    @Query(nativeQuery = true,value="select * from product p where p.id in :id")
  List<Product> getProductsByListId(List id);

    void deleteProductById(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Product p where p.id=:id")
   Product lockFindById(Long id);
    Optional <Product>findProductById(Long id);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryContaining(String category, Pageable pageable);
    Page<Product> findByNameContainingAndCategoryContaining(String name,
                                                                  String category,
                                                                  Pageable pageable);
    Page<Product> findProductByNameContainingAndCategoryContainingOrderByPriceAsc(String name, String category,Pageable pageable);
    Page<Product> findProductByNameContainingAndCategoryContainingOrderByPriceDesc(String name, String category,Pageable pageable);
}
