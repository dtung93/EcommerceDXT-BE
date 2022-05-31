package tech.getarrays.apimanager.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.model.Order;
import tech.getarrays.apimanager.payload.Users;
import tech.getarrays.apimanager.repo.CartRepo;
import tech.getarrays.apimanager.repo.OrderRepo;


import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartRepo cartRepo;

    public Page<Order> getOrders(Pageable paging) {
        return orderRepo.getAll(paging);
    }

    public Page<Order> getOrdersByPriceAsc(Pageable pageable) {
        return orderRepo.getAllPriceAsc(pageable);
    }

    public Page<Order> getOrdersByPriceDesc(Pageable paging) {
        return orderRepo.getAllPriceDesc(paging);
    }

    public Page<Order> getByUsernameByPriceDesc(String username, Pageable paging) {
        return orderRepo.findUsernameDesc(username, paging);
    }

    public Page<Order> getByUsernameByPriceAsc(String username, Pageable paging) {
        return orderRepo.findUsernameAsc(username, paging);
    }

    public Order postOrder(Order order) {
        return orderRepo.save(order);
    }

    public Page<Order> getAllOrders(Users users, Pageable paging) {
        Page<Order> orders = null;
        if (
                (users.getUsername() == null||users.getUsername()=="") &&
                        users.getPage() == null
                        && users.getPageSize() == null
                        && users.getSort() == null
        )
            orders = this.getOrders(paging);
        else if (
                (users.getUsername() == null||users.getUsername()=="")
                        && users.getSort() != null
        ) {
            if (users.getSort().equals("asc") )
                orders = this.getOrdersByPriceAsc(paging);
            if (users.getSort().equals("desc") ) orders = this.getOrdersByPriceDesc(paging);

        } else if (
                users.getUsername() != null
                        && users.getSort() == null
        )
            orders = this.getOrderByUsername(users.getUsername(), paging);
        else if (
                users.getUsername() != null
                        && users.getSort() != null
        ) {
            if (users.getSort().equals("asc") ) orders = this.getByUsernameByPriceAsc(users.getUsername(), paging);
            if (users.getSort().equals("desc")) orders = this.getByUsernameByPriceDesc(users.getUsername(), paging);
        } else
            orders = this.getOrders(paging);
        return orders;
    }

    public Order getOrderById(Long id) {

        return orderRepo.getOrderById(id);
    }

    public Page<Order> getOrderByUsername(String username, Pageable paging) {
        return orderRepo.findOrderByUsername(username, paging);
    }

    public Page<Order> getOrdersByUsername(String username, Pageable paging) {
        Page<Order> orders;
        orders = this.getOrderByUsername(username, paging);
        return orders;
    }

}
