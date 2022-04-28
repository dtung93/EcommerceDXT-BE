package tech.getarrays.apimanager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePassword {
    String oldPassword;
    String username;
    String newPassword;
}
