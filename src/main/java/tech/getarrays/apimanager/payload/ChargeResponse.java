package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="chargeResponse")
@Getter
@Setter
public class ChargeResponse {
    @Id
    @GeneratedValue
    private String Id;
    private String transactionId;
    private String username;
    private Date   orderDate;
    private Double total;
    private String status;


}
