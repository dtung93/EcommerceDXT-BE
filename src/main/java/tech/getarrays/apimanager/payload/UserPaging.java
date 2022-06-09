package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPaging {
 public String username;
 public Integer page;
 public Integer pageSize;
 public String sort;
}
