package tech.getarrays.apimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.model.CartProduct;
import tech.getarrays.apimanager.model.Product;
import tech.getarrays.apimanager.repo.OrderRepo;
import tech.getarrays.apimanager.repo.ProductRepo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CartService {
    @Autowired
    private OrderRepo cartRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;

    public Cart createCart(String username){
        Cart newCart=new Cart();
        newCart.setUsername(username);
        newCart.setStatus(false);
        orderRepo.save(newCart);
        return newCart;
    }

    public Cart getCartByUsername(String username){
        Cart cart = orderRepo.getCartByUser(username);
        if(cart==null){
         cart = this.createCart(username);
        }
        return cart;
    }
    public void updateCart(Cart cart){
        orderRepo.save(cart);
    }
    public void getCartProducts(List id){
        productRepo.getProductsByListId(id);
    }


    public Cart addProduct(Long id,String username) {
        Product newProduct = productRepo.findProductById(id).get();
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(newProduct);
        Cart cart=this.getCartByUsername(username);
        Set<CartProduct>items =cart.getItems();
                items.add(cartProduct);
     cart.setItems(items);
     this.updateCart(cart);
     return cart;

    }
}
