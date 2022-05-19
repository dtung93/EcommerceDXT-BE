package tech.getarrays.apimanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.model.Order;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.service.CartService;
import tech.getarrays.apimanager.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping("/new-order")
    public ResponseEntity<Order> newOrder(@RequestBody Order order){
        order.setStatus("1");
      Order newOrder= orderService.postOrder(order);
      return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/all-orders")
    public ResponseEntity<?>getAllOrders() throws Exception {
    try{    List<Order> order=orderService.getAllOrders();
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    catch (Exception e){
        return new ResponseEntity<>(new MessageResponse("No users found"),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }
//    public List<Order> getAllOrders(){
//        return  orderService.getAllOrders();
//    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id){
      Order orderDetail=orderService.getOrderById(id);
      return new ResponseEntity<>(orderDetail,HttpStatus.OK);
    };

    @GetMapping("/my-order")
    public List<Order> getOrderByCartId(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        var orders =  orderService.getOrdersByUsername(username);
        return orders;
    }


}
