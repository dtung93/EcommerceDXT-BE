package tech.getarrays.apimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.model.Cart;
import tech.getarrays.apimanager.model.CartProduct;
import tech.getarrays.apimanager.model.Product;
import tech.getarrays.apimanager.repo.CartProductRepo;
import tech.getarrays.apimanager.repo.CartRepo;
import tech.getarrays.apimanager.repo.ProductRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartProductRepo cartProductRepo;

    public Cart createCart(String username){
        Cart newCart=new Cart();
        newCart.setUsername(username);
        newCart.setStatus(false);
        cartRepo.save(newCart);
        return newCart;
    }


    public Cart getCartByUsername(String username){
        Cart cart = cartRepo.getCartByUser(username);
        if(cart==null){
         cart = this.createCart(username);
        }
        return cart;
    }
    public Cart updateCart(Cart cart){
      Integer totalQuantity=cart.getItems().stream().mapToInt(x->x.getQuantity()).sum();
      Double grandTotal=cart.getItems().stream().mapToDouble(x->x.getTotal()).sum();
      cart.setTotalItems(totalQuantity);
      cart.setGrandTotal(grandTotal);
      Cart updatedCart=cartRepo.save(cart);
      return updatedCart;
    }
   public void removeAllProducts(Cart cart){

        cartProductRepo.deleteAllProducts(cart.getId());;}
    public void updateCartProduct(CartProduct cartProduct){cartProductRepo.save(cartProduct);}

    public Cart emptyCart(String username){
       Cart cart= this.getCartByUsername(username);
        this.removeAllProducts(cart);
      cart.setTotalItems(0);
      cart.setGrandTotal(0.0);
      Cart updatedCart=cartRepo.save(cart);
      return updatedCart;
  }

    public void getCartProducts(List id){
        productRepo.getProductsByListId(id);
    }
    public void removeCartProduct(CartProduct cardProduct){cartProductRepo.deleteCartProduct(cardProduct.getId());}

    public Cart addProduct(Long id,String username) {
        Cart cart=this.getCartByUsername(username);
        CartProduct cartItem=this.findProductById(id,cart);
        if(cartItem==null) {
            Product newProduct = productRepo.findProductById(id).orElse(null);
            CartProduct cartProduct= new CartProduct();
            cartProduct.setProduct(newProduct);
            cartProduct.setQuantity(1);
            cartProduct.setTotal(newProduct.getPrice().doubleValue());
            cart.addCartProduct(cartProduct);
        }
        else{
            Integer quantity=cartItem.getQuantity()+1;
           cartItem.setQuantity(quantity);
           cartItem.setTotal(quantity * cartItem.getProduct().getPrice().doubleValue());
           this.updateCartProduct(cartItem);
        }
    return  this.updateCart(cart);
    }
    public Cart setProductQuantity(String username, Long id, Integer quantity){
        Cart cart=this.getCartByUsername(username);
        CartProduct cartItem=this.findProductById(id,cart);
        Integer newQuantity=quantity;
        cartItem.setQuantity(newQuantity);
        if(cartItem.getQuantity()==0){
            cart.removeCartProduct(cartItem);
            this.removeCartProduct(cartItem);
        }else {
            cartItem.setTotal(newQuantity * cartItem.getProduct().getPrice().doubleValue());
            this.updateCartProduct(cartItem);
        }
        return this.updateCart(cart);
    }

    public Cart addProductByQuantity(String username, Long id ,Integer addQuantity){
        Cart cart=this.getCartByUsername(username);
        CartProduct cartItem=this.findProductById(id,cart);
        if(cartItem==null){
            Product newProduct=productRepo.findProductById(id).get();
            CartProduct cartProduct=new CartProduct();
            cartProduct.setProduct(newProduct);
            cartProduct.setQuantity(addQuantity);
            cartProduct.setTotal(addQuantity* newProduct.getPrice().doubleValue());
            cart.addCartProduct(cartProduct);
        }
        else{
            Integer newQuantity=cartItem.getQuantity()+addQuantity;
            cartItem.setQuantity(newQuantity);
            cartItem.setTotal(newQuantity*cartItem.getProduct().getPrice().doubleValue());
            this.updateCartProduct(cartItem);
        }
        return this.updateCart(cart);
}
   public Cart removeProductQuantityByOne(String username, Long id){
       Cart cart=this.getCartByUsername(username);
       CartProduct cartItem=this.findProductById(id,cart);
           Integer quantity=cartItem.getQuantity()-1;
           cartItem.setQuantity(quantity);
           if(cartItem.getQuantity()==0){
               cart.removeCartProduct(cartItem);
               this.removeCartProduct(cartItem);
           }
           else {
               cartItem.setTotal(quantity * cartItem.getProduct().getPrice().doubleValue());
               this.updateCartProduct(cartItem);
           }
            return this.updateCart(cart);
   }

   public Cart removeProduct(String username, Long id){
       Cart cart=this.getCartByUsername(username);
       CartProduct cartItem=this.findProductById(id,cart);
       cart.removeCartProduct(cartItem);
      this.removeCartProduct(cartItem);
      return this.updateCart(cart);
   }

    public CartProduct findProductById(Long id,Cart cart){
       return cart.getItems().stream().filter(cp->cp.getProduct().getId().equals(id)).findFirst().orElse(null);
    }
}
