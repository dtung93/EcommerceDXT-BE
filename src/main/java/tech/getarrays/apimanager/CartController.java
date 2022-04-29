package tech.getarrays.apimanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.model.HandleCart;
import tech.getarrays.apimanager.model.HandleProduct;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.service.CartService;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/cart/add-item")
    public ResponseEntity<Cart> addToCart(@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.addProduct(handleProduct.getId(),username), HttpStatus.OK);
    }

    @PostMapping("/cart/add-item-quantity")
    public ResponseEntity<Cart> addToCartByQuantity(@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.addProductByQuantity(username, handleProduct.getId(), handleProduct.getQuantity()),HttpStatus.OK);
    }
    @GetMapping("/cart/get-cart")
    public ResponseEntity<Cart> getCart(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.getCartByUsername(username), HttpStatus.OK);
    }

    @PostMapping("cart/remove")
    public ResponseEntity<Cart> removeFromCartByOne (@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.removeProductQuantityByOne(username,handleProduct.getId()),HttpStatus.OK);
    }
    @PostMapping("cart/remove-item")
    public ResponseEntity<Cart> removeFromCart (@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.removeProduct(username,handleProduct.getId()),HttpStatus.OK);
    }
    @PostMapping("cart/empty-cart")
    public ResponseEntity<Cart> emptyCart(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(cartService.emptyCart(username),HttpStatus.OK);
    }
    @PostMapping("cart/set-item-quantity")
    public ResponseEntity<Cart> setQuantity(@RequestBody HandleProduct handleProduct){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>(cartService.setProductQuantity(username, handleProduct.getId(), handleProduct.getQuantity()),HttpStatus.OK);
    }


}
