package tech.getarrays.apimanager.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    User user;
    private Set<Role> roles = new HashSet<>();
    private String name;
    private String email;
    private String phone;
    private String address;
}
