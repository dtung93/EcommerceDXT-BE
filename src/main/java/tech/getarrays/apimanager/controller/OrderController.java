package tech.getarrays.apimanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.exception.ResponseError;
import tech.getarrays.apimanager.exception.StatusCode;
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
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        ResponseError responseError = new ResponseError();
        responseError.setErrorMessage(e.getMessage());
        responseError.setStatusCode(StatusCode.InternalError);
        responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
        logger.error(e.getMessage(),e);
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

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
