package tech.getarrays.apimanager.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name="orders")
@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private Long id;


    @JoinColumn(name="cart_id",referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.MERGE,fetch =FetchType.EAGER)
    private Cart cart;


    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="address")
    private String address;

    @Column(name="email")
    private String email;

    @Column(name="date")
    private Date date;

    @Column(name="status")
    private String status;


}
