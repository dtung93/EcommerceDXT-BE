package tech.getarrays.apimanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.model.HandleCart;
import tech.getarrays.apimanager.model.HandleProduct;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.model.Product;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAnyRole('USER','MODERATOR','ADMIN','MASTER')")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/add-item")
    public ResponseEntity<Cart> addToCart(@RequestBody HandleProduct handleProduct)  {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.addProduct(handleProduct.getId(),username), HttpStatus.OK);
    }

    @PostMapping("/add-item-quantity")
    public ResponseEntity<Cart> addToCartByQuantity(@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.addProductByQuantity(username, handleProduct.getId(), handleProduct.getQuantity()),HttpStatus.OK);
    }
    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.getCartByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeFromCartByOne (@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.removeProductQuantityByOne(username,handleProduct.getId()),HttpStatus.OK);
    }
    @PostMapping("/remove-item")
    public ResponseEntity<Cart> removeFromCart (@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.removeProduct(username,handleProduct.getId()),HttpStatus.OK);
    }
    @DeleteMapping("/empty-cart")
    public ResponseEntity<Cart> emptyCart(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(cartService.emptyCart(username),HttpStatus.OK);
    }
    @PostMapping("/set-item-quantity")
    public ResponseEntity<Cart> setQuantity(@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.setProductQuantity(username, handleProduct.getId(), handleProduct.getQuantity()),HttpStatus.OK);
    }

}
