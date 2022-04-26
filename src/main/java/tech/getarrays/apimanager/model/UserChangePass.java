package tech.getarrays.apimanager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePass {
    String oldPassword;
    String username;
    String newPassword;
}
