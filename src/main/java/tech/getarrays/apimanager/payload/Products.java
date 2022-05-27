package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Products {
    public Integer page;
    public Integer pageSize;
    public String productName;
    public String category;
    public String sort;
}
