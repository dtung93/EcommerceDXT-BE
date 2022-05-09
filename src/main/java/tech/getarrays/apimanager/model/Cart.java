package tech.getarrays.apimanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name="cart")
@Entity
@Getter
@Setter
public class Cart implements Serializable {
 @Id
 @GeneratedValue
 @Column(name="id")
 private Long Id;

// @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
// @JoinColumn(name = "user_id", referencedColumnName = "id")
// private User user;
//
// @ManyToMany(cascade = CascadeType.ALL,fetch =FetchType.LAZY)
// @JoinTable(name="cart_product",joinColumns = @JoinColumn(name="cart_id"),inverseJoinColumns = @JoinColumn(name="product_id"))
 @Column(name="username")
 private String username;

 @OneToMany(cascade = CascadeType.ALL,fetch =FetchType.LAZY)
 @JoinColumn(name="cart_id",referencedColumnName = "id")
 private Set<CartProduct> items;

 @Column(name="totalItems")
 private Integer totalItems=0;

 @Column(name="grandTotal")
 private Double grandTotal;

 @Column(name="status")
 private boolean status;

 public void addCartProduct(CartProduct cp){
   this.items.add(cp);
 }

 public void removeCartProduct(CartProduct cp){

  this.items.remove(cp);
 }

}
