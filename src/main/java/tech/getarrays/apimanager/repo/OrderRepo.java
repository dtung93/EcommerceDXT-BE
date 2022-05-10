package tech.getarrays.apimanager.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tech.getarrays.apimanager.model.Order;


import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends CrudRepository<Order,Long>, JpaRepository<Order,Long>, PagingAndSortingRepository<Order,Long> {
   List<Order> findAll();

    Order findOrderById(Long id);

    @Query(nativeQuery = true,value = "select o.* from cart c join orders o where c.id=o.cart_id and c.status=1 and c.username=:username")
    List<Order> findOrderByUsername(String username);

    @Query(nativeQuery = true,value="select * from orders o where o.cart_id=:id")
    List<Order>findByCartId(Long id);

}
