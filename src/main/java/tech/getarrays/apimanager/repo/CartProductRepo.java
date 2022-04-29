package tech.getarrays.apimanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import tech.getarrays.apimanager.model.CartProduct;

public interface CartProductRepo extends CrudRepository<CartProduct,Long>, JpaRepository<CartProduct,Long>, PagingAndSortingRepository<CartProduct,Long> {
    @Query(nativeQuery = true,value = "DELETE from cart_product c where c.id=:id")
    @Modifying
    void deleteCartProduct(@Param("id") Long id);


    @Query(nativeQuery = true,value="delete from cart_product c where c.cart_id=:id")
    @Modifying
    void deleteAllProducts(Long id);
}
