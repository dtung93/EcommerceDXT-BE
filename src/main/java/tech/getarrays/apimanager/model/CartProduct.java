package tech.getarrays.apimanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name="cart_product")
@Entity
@Getter
@Setter
public class CartProduct {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long Id;


    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name="product_id",referencedColumnName = "id")
    private Product product;

    @Column(name="quantity")
    private Integer quantity;
}
