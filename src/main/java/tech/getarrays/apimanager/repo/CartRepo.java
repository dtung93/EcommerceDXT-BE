package tech.getarrays.apimanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tech.getarrays.apimanager.model.Cart;

public interface CartRepo extends CrudRepository<Cart,Long>, JpaRepository<Cart,Long>, PagingAndSortingRepository<Cart,Long> {

    @Query(nativeQuery = true,value="select * from cart c where c.username=:username and c.status=0")
    public Cart getCartByUser(String username);



}
