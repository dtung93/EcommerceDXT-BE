package tech.getarrays.apimanager.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.model.Order;
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

    public Order postOrder(Order order){
        return orderRepo.save(order);
    }
    public List<Order> getAllOrders(){

        return orderRepo.findAll();
    }

    public Order getOrderById(Long id){

        return orderRepo.findOrderById(id);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepo.findOrderByUsername(username);
    }
}
