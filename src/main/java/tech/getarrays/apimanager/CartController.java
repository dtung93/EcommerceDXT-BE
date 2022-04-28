package tech.getarrays.apimanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.service.CartService;
import tech.getarrays.apimanager.service.UserService;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/cart/add-to-cart/{id}")
    public ResponseEntity<Cart> addToCart(@PathVariable("id") Long id){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.addProduct(id,username), HttpStatus.OK);
    }
}
