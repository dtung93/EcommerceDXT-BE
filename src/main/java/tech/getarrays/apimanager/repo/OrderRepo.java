package tech.getarrays.apimanager.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tech.getarrays.apimanager.model.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends CrudRepository<Order, Long>, JpaRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {
    List<Order> findAll();

    @Query(nativeQuery = true, value = "select * from orders ")
    Page<Order> getAll(Pageable pageable);


    @Query(nativeQuery = true,value="select * from orders o where o.id=:id")
    <Optional>Order getOrderById(Long id);

    @Query(nativeQuery = true, value = "select * from orders o join cart c where c.id=o.cart_id and c.status=1 and c.username like concat('%',:username,'%')")
    Page<Order> findOrderByUsername(String username, Pageable pageable);


    @Query(nativeQuery = true, value = "select * from orders o join cart c where c.id=o.cart_id and c.status=1 order by c.grand_total desc")
    Page<Order> getAllPriceDesc(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from orders o join cart c where c.id=o.cart_id and c.status=1 order by c.grand_total")
    Page<Order> getAllPriceAsc(Pageable paging);

    @Query(nativeQuery = true, value = "select * from orders o join cart c where c.id=o.cart_id and c.status=1 and c.username like concat('%',:username,'%') order by c.grand_total desc")
    Page<Order> findUsernameDesc(String username, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from orders o join cart c where c.id=o.cart_id and c.status=1 and c.username like concat('%',:username,'%') order by c.grand_total")
    Page<Order> findUsernameAsc(String username, Pageable pageable);



}
