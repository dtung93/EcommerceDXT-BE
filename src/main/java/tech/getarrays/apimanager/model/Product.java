package tech.getarrays.apimanager.model;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(nullable=false,updatable=false)
    private Long id;
    private String name;

//    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
//    private Set<Cart> carts;


    private String category;
    private String description;
    private String img;
    private Number qty;
    private Number price;
    @Column(nullable=false,updatable=false)
    private String productcode;
    public Product(){}
    public Product(String name, String category, String description, String img, Number qty, Number price, String productcode) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.img = img;
        this.qty = qty;
        this.price = price;
        this.productcode = productcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Number getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

//    @Override
//    public String toString() {
//        return "Product{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", category='" + category + '\'' +
//                ", description='" + description + '\'' +
//                ", img='" + img + '\'' +
//                ", qty=" + qty +
//                ", price=" + price +
//                ", productcode='" + productcode + '\'' +
//                '}';
//    }
}
