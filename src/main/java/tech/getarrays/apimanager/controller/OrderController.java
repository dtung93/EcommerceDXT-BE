package tech.getarrays.apimanager.controller;

import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.exception.ResponseError;
import tech.getarrays.apimanager.exception.StatusCode;
import tech.getarrays.apimanager.model.Order;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.payload.ResponseData;
import tech.getarrays.apimanager.payload.UserPaging;
import tech.getarrays.apimanager.payload.UserPaging;
import tech.getarrays.apimanager.service.CartService;
import tech.getarrays.apimanager.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Order> newOrder(@RequestBody Order order) {
        order.setStatus("1");
        Order newOrder = orderService.postOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PostMapping("/all-orders")
    public ResponseEntity<?> getAllOrders(@RequestBody UserPaging users) {
        ResponseData responseData = new ResponseData();
        try {
            List<Order> listorders;
            Pageable paging = PageRequest.of(
                    (users.getPage() != null) ? users.getPage() : 0,
                    (users.getPageSize() != null) ? users.getPageSize() : 10
            );
            Page<Order> pageOrders=null;
            pageOrders = orderService.getAllOrders(users, paging);
            listorders = pageOrders.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("currentPage", pageOrders.getNumber());
            response.put("totalItems", pageOrders.getTotalElements());
            response.put("orders", listorders);
            response.put("totalPages", pageOrders.getTotalPages());
            responseData.setStatusCode(StatusCode.SuccessfulRequest);
            responseData.setMapData("response", response);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            ResponseError error = new ResponseError();
            error.setErrorCode(StatusCode.InternalError);
            error.setErrorMessage(e.getMessage());
            error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        Order orderDetail = orderService.getOrderById(id);
        return new ResponseEntity<>(orderDetail, HttpStatus.OK);
    }


    @PostMapping("/my-order")
    public ResponseEntity<?> getOrderByUsername(@RequestBody UserPaging users) {
        ResponseData responseData = new ResponseData();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            List<Order> orders;
            Pageable paging = PageRequest.of(
                    (users.getPage() != null) ? users.getPage() : 0,
                    (users.getPageSize() != null) ? users.getPageSize() : 8
            );
            Page<Order> pageOrders;
            pageOrders = orderService.getOrdersByUsername(username, paging);
            orders = pageOrders.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            response.put("currentPage", pageOrders.getNumber());
            response.put("totalItems", pageOrders.getTotalElements());
            response.put("totalPages", pageOrders.getTotalPages());
            responseData.setStatusCode(StatusCode.SuccessfulRequest);
            responseData.setMapData("data", response);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            ResponseError error = new ResponseError();
            error.setErrorCode(StatusCode.InternalError);
            error.setErrorMessage(e.getMessage());
            error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
