package tech.getarrays.apimanager.model;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
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
    private String category;
    private String description;
    private String img;
    private Integer qty;
    private Double price;
    private String editBy;
    private Date  date;
    @Column(nullable=false,updatable=false)
    private String productcode;
    public void updateQuantity(Integer quantity) {
        this.qty=this.qty.intValue()- quantity;
    }
}
