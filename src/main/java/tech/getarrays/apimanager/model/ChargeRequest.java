package tech.getarrays.apimanager.model;

import lombok.Data;

@Data
public class ChargeRequest {
    public enum Currency {
        EUR, USD;
    }
    public String username;
    private String description;
    private Integer amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
}
